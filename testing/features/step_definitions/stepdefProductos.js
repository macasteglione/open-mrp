const assert = require("assert");
const { Given, When, Then } = require("cucumber");
const { hacerRequestHttp } = require("../../hacerRequestHttp");

Given(
    "que se ingresa el nuevo producto con {string}",
    function (nombre) {
        this.producto = JSON.stringify({
            nombre: nombre,
            tareas: []
        });
    }
);

Given(
    "que existen los talleres cuando se agrega la tarea con {string}, {int}, {int} y {string} para el producto {string}",
    function (nombreTarea, orden, tiempo, tipoEquipo, nombreProducto) {
        let res = hacerRequestHttp('GET', `http://backend:8080/productos/nombre/${nombreProducto}`)
        let tipo = hacerRequestHttp('GET', `http://backend:8080/tiposEquipos/nombre/${tipoEquipo}`)

        this.producto = {
            id: res.data.id,
            nombre: nombreProducto,
            tareas: res.data.tareas
        };

        this.producto.tareas.push({
            nombre: nombreTarea,
            orden: orden,
            tiempo: tiempo,
            tipoEquipo: {
                id: tipo.data.id
            }
        });

        this.producto = JSON.stringify(this.producto);
    }
);

When("presiono el botón de guardar productos", function () {
    this.response = hacerRequestHttp(
        "POST",
        "http://backend:8080/productos",
        this.producto
    );
});

When("presiono el botón de guardar tareas", function () {
    this.response = hacerRequestHttp(
        "PUT",
        "http://backend:8080/productos",
        this.producto
    );
});

Then(
    "se espera el siguiente {int} con la {string} productos",
    function (status, respuesta) {
        assert.strictEqual(this.response.status, status);
        assert.strictEqual(this.response.message, respuesta);
    }
);