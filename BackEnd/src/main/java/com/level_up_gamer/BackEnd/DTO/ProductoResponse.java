package com.level_up_gamer.BackEnd.DTO;

public class ProductoResponse {
    
    private Long id;
    private String nombre;
    private Integer precio;
    private String categoria;
    private String imagen;
    private String descripcion;
    private Integer stock;
    private Boolean destacado;
    private Boolean nuevo;
    private Boolean oferta;
    private Integer precioOferta;
    private String especificaciones;
    
    // Constructor
    public ProductoResponse(Long id, String nombre, Integer precio, String categoria, 
                           String imagen, String descripcion, Integer stock, 
                           Boolean destacado, Boolean nuevo, Boolean oferta, 
                           Integer precioOferta, String especificaciones) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.stock = stock;
        this.destacado = destacado;
        this.nuevo = nuevo;
        this.oferta = oferta;
        this.precioOferta = precioOferta;
        this.especificaciones = especificaciones;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public Boolean getDestacado() { return destacado; }
    public void setDestacado(Boolean destacado) { this.destacado = destacado; }
    
    public Boolean getNuevo() { return nuevo; }
    public void setNuevo(Boolean nuevo) { this.nuevo = nuevo; }
    
    public Boolean getOferta() { return oferta; }
    public void setOferta(Boolean oferta) { this.oferta = oferta; }
    
    public Integer getPrecioOferta() { return precioOferta; }
    public void setPrecioOferta(Integer precioOferta) { this.precioOferta = precioOferta; }
    
    public String getEspecificaciones() { return especificaciones; }
    public void setEspecificaciones(String especificaciones) { this.especificaciones = especificaciones; }
}
