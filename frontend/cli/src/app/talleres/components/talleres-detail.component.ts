import { CommonModule, Location } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { Taller } from '../interfaces/taller';
import { ActivatedRoute } from '@angular/router';
import { TalleresService } from '../interfaces/talleres.service';
import { ModalService } from '../../modal/modal.service';
import { Observable, catchError, debounceTime, distinctUntilChanged, map, of, switchMap, tap } from 'rxjs';
import { TipoEquipo } from '../tipos/interfaces/tipoEquipo';
import { TipoEquiposService } from '../tipos/interfaces/tipoEquipos.service';
import { Equipo } from '../interfaces/equipo';

@Component({
  selector: 'app-talleres-detail',
  standalone: true,
  imports: [CommonModule,
    FormsModule,
    NgbTypeaheadModule
  ],
  templateUrl: "talleres-detail.component.html",
  styleUrl: "talleres-detail.component.css"
})
export class TalleresDetailComponent {
  taller!: Taller;
  searching: boolean = false;
  searchFailed: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private tallerService: TalleresService,
    private tipoEquipoService: TipoEquiposService,
    private location: Location,
    private modalService: ModalService
  ) { }

  ngOnInit() {
    this.get();
  }

  get() {
    const id = this.route.snapshot.paramMap.get("id");
    if (id === "new") {
      this.taller = <Taller>{
        codigo: "",
        nombre: "",
        equipos: <Equipo[]>[],
      };
    } else {
      this.tallerService
        .get(parseInt(id!))
        .subscribe(
          (dataPackage) => {
            this.taller = <Taller>dataPackage.data;
          }
        );
    }
  }

  goBack() {
    this.location.back();
  }

  save() {
    this.tallerService.save(this.taller).subscribe((dataPackage) => {
      this.taller = <Taller>dataPackage.data;
      this.goBack();
    });
  }

  searchTipoEquipo = (text$: Observable<string>): Observable<any[]> =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => (this.searching = true)),
      switchMap((term) =>
        this.tipoEquipoService
          .search(term)
          .pipe(
            map((response) => {
              let equipos = <Equipo[]>response.data;
              return equipos;
            })
          )
          .pipe(
            tap(() => (this.searchFailed = false)),
            catchError(() => {
              this.searchFailed = true;
              return of([]);
            })
          )
      ),
      tap(() => (this.searching = false))
    );

  resultFormat(value: any) {
    return value.nombre;
  }

  inputFormat(value: any) {
    return value ? value.nombre : null;
  }

  addEquipo() {
    this.taller.equipos.push(<Equipo>{
      codigo: "",
      tipoEquipo: <TipoEquipo>{}
    });
  }

  removeEquipo(equipo: Equipo) {
    this.modalService
      .confirm(
        "Eliminar equipo",
        "¿Esta seguro de borrar este equipo?",
        "El cambio no se confirmara hasta que no guarde el taller."
      )
      .then(() => {
        let equipos = this.taller.equipos;
        equipos.splice(equipos.indexOf(equipo), 1);
      });
  }
}
