import { Component } from '@angular/core';
import { Producto } from '../interfaces/producto';
import { ActivatedRoute } from '@angular/router';
import { ProductosService } from '../interfaces/productos.service';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Tarea } from '../interfaces/tarea';
import { ModalService } from '../../modal/modal.service';
import { Observable, catchError, debounceTime, distinctUntilChanged, map, of, switchMap, tap } from 'rxjs';
import { TipoEquipo } from '../../talleres/tipos/interfaces/tipoEquipo';
import { TipoEquiposService } from '../../talleres/tipos/interfaces/tipoEquipos.service';
import { NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-productos-detail',
  standalone: true,
  imports: [CommonModule,
    FormsModule,
    NgbTypeaheadModule],
  templateUrl: "productos-detail.component.html",
  styleUrl: "productos-detail.component.css"
})
export class ProductosDetailComponent {
  producto!: Producto;
  searching: boolean = false;
  searchFailed: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private productoService: ProductosService,
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
      this.producto = <Producto>{
        nombre: "",
        tareas: <Tarea[]>[],
      };
    } else {
      this.productoService
        .get(parseInt(id!))
        .subscribe(
          (dataPackage) => {
            this.producto = <Producto>dataPackage.data;
          }
        );
    }
  }

  goBack() {
    this.location.back();
  }

  save() {
    this.productoService.save(this.producto).subscribe((dataPackage) => {
      this.producto = <Producto>dataPackage.data;
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
              let tareas = <Tarea[]>response.data;
              return tareas;
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

  addTarea() {
    this.producto.tareas.push(<Tarea>{
      nombre: "",
      tipoEquipo: <TipoEquipo>{}
    });
  }

  removeTarea(tarea: Tarea) {
    this.modalService
      .confirm(
        "Eliminar tarea",
        "¿Esta seguro de borrar este tarea?",
        "El cambio no se confirmara hasta que no guarde el producto."
      )
      .then(() => {
        let tareas = this.producto.tareas;
        tareas.splice(tareas.indexOf(tarea), 1);
      });
  }
}
