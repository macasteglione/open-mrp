const assert = require("assert");
const { Given, When, Then } = require("cucumber");
const { hacerRequestHttp } = require("../../hacerRequestHttp");

const Estado = {
    PENDIENTE: "PENDIENTE",
    PLANIFICADO: "PLANIFICADO",
    NO_PLANIFICABLE: "NO_PLANIFICABLE",
    FINALIZADO: "FINALIZADO",
};

Given("que existe el {string}", function (producto) {
    this.producto = hacerRequestHttp(
        "GET",
        `http://backend:8080/productos/nombre/${producto}`
    );
});

Given("el cliente con {int}", function (cuit) {
    this.cliente = hacerRequestHttp(
        "GET",
        `http://backend:8080/clientes/cuit/${cuit}`
    );
});

When(
    "se solicita generar un pedido al cliente {int} con fecha de pedido {string} para entregar en la fecha {string} la cantidad de {int} del producto {string}",
    function (cuit, fechaPedido, fechaEntrega, cantidad, producto) {
        this.pedido = {
            fechaPedido: fechaPedido,
            fechaEntrega: fechaEntrega,
            cantidad: cantidad,
            producto: {
                id: this.producto.data.id,
            },
            cliente: {
                id: this.cliente.data.id,
            },
            estado: Estado.PENDIENTE,
            ordenTrabajo: [],
        };

        this.response = hacerRequestHttp(
            "POST",
            "http://backend:8080/pedidos",
            JSON.stringify(this.pedido)
        );
    }
);

Then(
    "se espera el siguiente {int} con la {string} pedidos",
    function (status, respuesta) {
        assert.strictEqual(this.response.status, status);
        assert.strictEqual(this.response.message, respuesta);
    }
);
