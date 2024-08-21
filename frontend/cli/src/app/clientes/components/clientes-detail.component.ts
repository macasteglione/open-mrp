import { CommonModule, Location, UpperCasePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Cliente } from '../interfaces/cliente';
import { ActivatedRoute } from '@angular/router';
import { ClienteService } from '../interfaces/clientes.service';

@Component({
  selector: 'app-clientes-detail',
  standalone: true,
  imports: [UpperCasePipe, FormsModule, CommonModule],
  templateUrl: "clientes-detail.component.html",
  styleUrl: "clientes-detail.component.css"
})
export class ClientesDetailComponent {
  cliente!: Cliente;

  constructor(
    private route: ActivatedRoute,
    private clienteService: ClienteService,
    private location: Location
  ) { }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.clienteService.save(this.cliente).subscribe((dataPackage) => {
      this.cliente = <Cliente>dataPackage.data;
      this.goBack();
    });
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get("id")!;
    if (id === "new") this.cliente = <Cliente>{ nombre: "" };
    else {
      this.clienteService
        .get(parseInt(id!))
        .subscribe(
          (dataPackage) =>
            (this.cliente = <Cliente>dataPackage.data)
        );
    }
  }

  ngOnInit() {
    this.get();
  }
}
