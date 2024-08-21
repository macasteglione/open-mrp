const assert = require("assert");
const { Given, When, Then } = require("cucumber");
const { hacerRequestHttp } = require("../../hacerRequestHttp");

Given(
    "que se ingresa el cliente con {string} y {int}",
    function (razonSocial, cuit) {
        this.cliente = JSON.stringify({
            nombre: razonSocial,
            cuit: cuit,
        });
    }
);

When("presiono el botón de guardar clientes", function () {
    this.response = hacerRequestHttp(
        "POST",
        "http://backend:8080/clientes",
        this.cliente
    );
});

Then(
    "se espera el siguiente {int} con la {string} clientes",
    function (status, respuesta) {
        assert.strictEqual(this.response.status, status);
        assert.strictEqual(this.response.message, respuesta);
    }
);