const { Given, When, Then } = require("cucumber");
const { hacerRequestHttp } = require("../../hacerRequestHttp");
const assert = require("assert");

Given(
    "el pedido con {int} {string} {string} {string} {int}",
    function (cuit, fechaPedido, fechaEntrega, producto, cantidad) {
        this.response = hacerRequestHttp(
            "GET",
            `http://backend:8080/pedidos/search/${cuit}/${fechaPedido}/${fechaEntrega}/${producto}/${cantidad}`
        );
    }
);

Given("el {string} PlanificacionPedido", function (taller) {
    this.taller = taller;
});

Given(
    "tomando como base de planificación la fecha {string} PlanificacionPedido",
    function (fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
);

When(
    "se solicita planificar con esquema de pronta entregar PlanificacionPedido",
    function () {
        return "pending";
    }
);

When(
    "se solicita planificar con entrega tardía EDF PlanificacionPedido",
    function () {
        this.actualAnswer = hacerRequestHttp(
            "GET",
            `http://backend:8080/planificador/pedidos/planificarPedidoTardia/${this.response.data.id}`
        );
    }
);

Then(
    "se obtiene la {int} con {int} y {string} PlanificacionPedido",
    function (planificación, status, respuesta) {
        assert.equal(this.actualAnswer.status, status);
        assert.equal(this.actualAnswer.message, respuesta);
    }
);
