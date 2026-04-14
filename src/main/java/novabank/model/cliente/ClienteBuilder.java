package novabank.model.cliente;

import java.time.LocalDate;

public class ClienteBuilder {

    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;
    private String fecha_creacion;

    public ClienteBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ClienteBuilder apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public ClienteBuilder dni(String dni) {
        this.dni = dni;
        return this;
    }

    public ClienteBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClienteBuilder telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public ClienteBuilder fecha_creacion(LocalDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion.toString();
        return this;
    }


    public Cliente build() {
        Cliente cliente = new Cliente();
        cliente.setNombre(this.nombre);
        cliente.setApellidos(this.apellidos);
        cliente.setDni(this.dni);
        cliente.setEmail(this.email);
        cliente.setTelefono(this.telefono);
        cliente.setFecha_creacion(LocalDate.parse(this.fecha_creacion));
        return cliente;
    }
}
