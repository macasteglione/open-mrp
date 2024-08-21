const assert = require("assert");
const { Given, When, Then } = require("cucumber");
const { hacerRequestHttp } = require("../../hacerRequestHttp");

Given(
    "que se ingresa el nuevo taller con {string} y {string}",
    function (codigo, nombre) {
        this.taller = JSON.stringify({
            codigo: codigo,
            nombre: nombre,
            equipos: [],
        });
    }
);

Given(
    "que existen los talleres cuando se agrega el equipo con {string} del {string} y {int} al taller {string}",
    function (codigoEquipo, tipoEquipo, capacidad, codigoTaller) {
        let res = hacerRequestHttp(
            "GET",
            `http://backend:8080/talleres/codigo/${codigoTaller}`
        );
        let tipo = hacerRequestHttp(
            "GET",
            `http://backend:8080/tiposEquipos/nombre/${tipoEquipo}`
        );

        this.taller = {
            id: res.data.id,
            codigo: codigoTaller,
            nombre: res.data.nombre,
            equipos: res.data.equipos,
        };

        this.taller.equipos.push({
            codigo: codigoEquipo,
            capacidad: capacidad,
            tipoEquipo: {
                id: tipo.data.id,
            },
        });

        this.taller = JSON.stringify(this.taller);
    }
);

When("presiono el botón de guardar talleres", function () {
    this.response = hacerRequestHttp(
        "POST",
        "http://backend:8080/talleres",
        this.taller
    );
});

When("presiono el botón de guardar equipos", function () {
    this.response = hacerRequestHttp(
        "PUT",
        "http://backend:8080/talleres",
        this.taller
    );
});

Then(
    "se espera el siguiente {int} con la {string} talleres",
    function (status, respuesta) {
        assert.strictEqual(this.response.status, status);
        assert.strictEqual(this.response.message, respuesta);
    }
);
