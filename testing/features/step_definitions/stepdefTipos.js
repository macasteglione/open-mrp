const assert = require("assert");
const { Given, When, Then } = require("cucumber");
const { hacerRequestHttp } = require("../../hacerRequestHttp");

Given("que se ingresa el tipo de equipo con {string}", function (nombre) {
    this.tipoEquipo = JSON.stringify({
        nombre: nombre,
    });
});

When("presiono el botón de guardar tipos", function () {
    this.response = hacerRequestHttp(
        "POST",
        "http://backend:8080/tiposEquipos",
        this.tipoEquipo
    );
});

Then(
    "se espera el siguiente {int} con la {string} tipos",
    function (status, respuesta) {
        assert.strictEqual(this.response.status, status);
        assert.strictEqual(this.response.message, respuesta);
    }
);