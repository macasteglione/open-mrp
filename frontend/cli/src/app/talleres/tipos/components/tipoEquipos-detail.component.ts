import { CommonModule, Location, UpperCasePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TipoEquipo } from '../interfaces/tipoEquipo';
import { ActivatedRoute } from '@angular/router';
import { TipoEquiposService } from '../interfaces/tipoEquipos.service';

@Component({
  selector: 'app-tipo-equipos-detail',
  standalone: true,
  imports: [UpperCasePipe, FormsModule, CommonModule],
  templateUrl: "tipoEquipos-detail.component.html",
  styleUrl: "tipoEquipos-detail.component.css"
})
export class TipoEquiposDetailComponent {
  tipoEquipo!: TipoEquipo;

  constructor(
    private route: ActivatedRoute,
    private tipoEquiposService: TipoEquiposService,
    private location: Location
  ) { }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.tipoEquiposService.save(this.tipoEquipo).subscribe((dataPackage) => {
      this.tipoEquipo = <TipoEquipo>dataPackage.data;
      this.goBack();
    });
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get("id")!;
    if (id === "new") this.tipoEquipo = <TipoEquipo>{ nombre: "" };
    else {
      this.tipoEquiposService
        .get(parseInt(id!))
        .subscribe(
          (dataPackage) =>
            (this.tipoEquipo = <TipoEquipo>dataPackage.data)
        );
    }
  }

  ngOnInit() {
    this.get();
  }
}
