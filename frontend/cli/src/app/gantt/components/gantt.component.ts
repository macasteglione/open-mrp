import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbTypeaheadModule } from "@ng-bootstrap/ng-bootstrap";
import { ChartType, GoogleChartsModule } from "angular-google-charts";

import { Observable } from "rxjs/internal/Observable";
import {
    catchError,
    debounceTime,
    distinctUntilChanged,
    filter,
    map,
    of,
    switchMap,
    tap,
} from "rxjs";
import { Taller } from "../../talleres/interfaces/taller";
import { GanttObject } from "../interfaces/GanttObject";
import { GanttService } from "../interfaces/gantt.service";
import { TalleresService } from "../../talleres/interfaces/talleres.service";
import { AppComponent } from "../../app.component";

@Component({
    selector: "app-gantt",
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        GoogleChartsModule,
        NgbTypeaheadModule,
    ],
    templateUrl: "gantt.component.html",
    styleUrl: "gantt.component.css",
})
export class GanttComponent {
    from!: Date;
    to!: Date;
    pixelsPerDay: number = 25000;
    width!: string;

    ganttObject!: GanttObject;
    taller!: Taller;
    searching: boolean = false;
    searchFailed: boolean = false;
    show: boolean = false;
    chart = {
        title: "Planificaciones",
        type: ChartType.Timeline,
        data: [] as [string, string, Date, Date][],
        options: {
            hAxis: {
                format: "dd MMM yyyy - HH:mm",
                gridlines: {
                    unit: "minute",
                    count: -1,
                },
            },
        },
    };

    constructor(
        private service: GanttService,
        private tallerService: TalleresService,
        private appComponent: AppComponent
    ) {}

    calculateChartWidth(): string {
        let width = this.ganttObject.cantDias * this.pixelsPerDay;
        if (width < 2000) width = 2000;
        return `${width}px`;
    }

    getGanttObjects(): void {
        this.appComponent.setLoading(true);

        this.service
            .getGanttObjects(this.taller.id, this.from, this.to)
            .subscribe((dataPackage) => {
                if (dataPackage.status != 200) {
                    this.show = false;
                    this.appComponent.setLoading(false);
                } else {
                    this.show = true;
                    this.ganttObject = <GanttObject>dataPackage.data;
                    this.width = this.calculateChartWidth();
                    this.updateChart();
                    this.appComponent.setLoading(false);
                }
            });
    }

    updateChart(): void {
        console.log(this.ganttObject);
        this.chart.data = this.ganttObject.planificaciones.map((gantt) => [
            gantt.equipo.codigo,
            `${gantt.pedido.producto.nombre} - ${gantt.pedido.id}`,
            new Date(gantt.inicio),
            new Date(gantt.fin),
        ]);
    }

    searchWorkshop = (text$: Observable<string>): Observable<any[]> =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            filter((term) => term.length >= 2),
            switchMap((term) =>
                this.tallerService
                    .search(term)
                    .pipe(
                        map((response) => {
                            let equipment = <Taller[]>response.data;
                            return equipment;
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
        return value ? value.codigo : null;
    }
}
