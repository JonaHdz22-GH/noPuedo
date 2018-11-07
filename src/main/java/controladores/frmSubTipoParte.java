
package controladores;

import acceso.SubTipoParteFacadeLocal;
import acceso.TipoParteFacade;
import acceso.TipoParteFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.SubTipoParte;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoParte;

/**
 *
 * @author jonahdz
 */
@Named(value="frmSubTipoParte")
@ViewScoped
public class frmSubTipoParte implements Serializable {
  
    frmSubTipoParte(){}
    
    @EJB
    private SubTipoParteFacadeLocal facadeLocal;
    private LazyDataModel<SubTipoParte> lazymodel;
    private SubTipoParte registroEntity;
    private boolean botonAgregar = true;
    private boolean botones = false;
    private boolean seleccion = false;
   
    @EJB
    private TipoParteFacadeLocal tipoParteFacadeLocal;
    
    
    @PostConstruct
    private void inicio() {
   
        registroEntity = new SubTipoParte();
        
        try {
            this.lazymodel = new LazyDataModel<SubTipoParte>() {
                
                @Override
                public Object getRowKey(SubTipoParte object) {
                    if (object != null) {
                        return object.getIdTipoParte();
                    }
                    return null;
                }
                
                @Override
                public SubTipoParte getRowData(String rowKey) {
                    if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                        try {
                            Integer buscado = new Integer(rowKey);
                            for (SubTipoParte reg : (List<SubTipoParte>) getWrappedData()) {
                                if (reg.getIdSubTipoParte().compareTo(buscado) == 0) {
                                    return reg;
                                }
                            }
                        } catch (Exception e) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                    return null;
                }
                
                @Override
                public List<SubTipoParte> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    List<SubTipoParte> salida = new ArrayList();
                    try {
                        if (facadeLocal != null) {
                            this.setRowCount(facadeLocal.count());
                            salida = facadeLocal.findRange(first,pageSize);
                        }
                    } catch (Exception e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                    }
                    return salida;
                }
                
            };
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    //@Deprecated
    public List<TipoParte> listaTipoParte(){
        List<TipoParte> salida = new ArrayList();
            try {
                if (tipoParteFacadeLocal != null) {
                    salida = tipoParteFacadeLocal.findAll();
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
            return salida;
    }
    
    
    /**
     *  Este metodo Domina la Funcionalidad del boton guardar mostrado en la pagina web index.xhtml 
     *  verifica que los registros que han sido ingresados en el formulario no sean nulos para asi no 
     *  generar ninguna excepcion en caso de excepcion no guardara el registro  es capturado por try-catch 
     *  y enviado para notificar el error al cliente.
     */
    public void guardar() {
        try {
            if (this.facadeLocal != null && this.registroEntity != null ) {
               if(this.facadeLocal.create(registroEntity)) {
                    inicio();
                }
            }
        } catch (Exception exp) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, exp.getMessage(),exp);
        }
    }

    /**
     *  Este metodo Domina la Funcionalidad del boton eliminar mostrado en la pagina web index.xhtml 
     *  verifica que los registros que han sido ingresados nos sean nulos para asi no generar excepcion
     *  en caso de excepcion es capturado por try-catch y enviado para notificar el error al cliente.
     */
    public void Eliminar() {
        try {
            if (this.registroEntity != null && this.facadeLocal != null) {
                if (this.facadeLocal.remove(registroEntity)) {
                    this.registroEntity = new SubTipoParte();
                    this.botones = false;
                    this.botonAgregar = true;
                    inicio();
                }
            }
        } catch (Exception exp) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, exp.getMessage(),exp);
        }
    }

    /**
     *  Este metodo Domina la Funcionalidad del boton modificar mostrado en la pagina web index.xhtml 
     *  verifica que los registros que han sido ingresados y que son requisitos no sean nulos para asi 
     *  no generar excepcion en caso de no cumplir con esto se genera una excepcion es capturado por 
     *  try-catch y enviado para notificar el error al cliente.
     */
    public void Modificar() {
        try {
            if (this.registroEntity != null && this.facadeLocal != null) {
                if (this.facadeLocal.edit(registroEntity)) {
                    this.botones = false;
                    this.botonAgregar = true;
                    inicio();  
                }
            }
        } catch (Exception exp) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, exp.getMessage(),exp);
        }
    }

    /**
     *  Este metodo Domina la Funcionalidad de los botones agregar y cancelar mostrado en la pagina web 
     *  index.xhtml cuando esta en inicio normal lo cual es dinamico mientras se necesite hacer diferentes
     *  operaciones, transacciones o mantenimiento de la entidad.
     */
    public void cambiarSeleccion() {
        this.botones = true;
        this.botonAgregar = false;    
    }
    
    /**
     *  El Metodo cancelar elimina los posibles registros que hayan sido ingresados en el formulario y muestra
     *  los botones editar y cancelar como se muestra al inicio de la pagina para empezar un nuevo registro o 
     *  realizar otra operacion con la pagina de mantenimiento de la entidad.
     */
    public void cancelar() {
        this.registroEntity = new SubTipoParte();
        this.botones=false;
        this.botonAgregar=true;
    }

    
    
    // <editor-fold defaultstate="collapsed" desc="Getters And Setters">

    public TipoParteFacadeLocal getTipoParteFacadeLocal() {
        return tipoParteFacadeLocal;
    }

    public void setTipoParteFacadeLocal(TipoParteFacadeLocal tipoParteFacadeLocal) {
        this.tipoParteFacadeLocal = tipoParteFacadeLocal;
    }
   
    
    


    public SubTipoParteFacadeLocal getFacadeLocal() {
        return facadeLocal;
    }

    public void setFacadeLocal(SubTipoParteFacadeLocal facadeLocal) {
        this.facadeLocal = facadeLocal;
    }

    public LazyDataModel<SubTipoParte> getLazymodel() {
        return lazymodel;
    }

    public void setLazymodel(LazyDataModel<SubTipoParte> lazymodel) {
        this.lazymodel = lazymodel;
    }

    public SubTipoParte getRegistroEntity() {
        return registroEntity;
    }

    public void setRegistroEntity(SubTipoParte registroEntity) {
        this.registroEntity = registroEntity;
    }

    public boolean isBotonAgregar() {
        return botonAgregar;
    }

    public void setBotonAgregar(boolean botonAgregar) {
        this.botonAgregar = botonAgregar;
    }

    public boolean isBotones() {
        return botones;
    }

    public void setBotones(boolean botones) {
        this.botones = botones;
    }

    public boolean isSeleccion() {
        return seleccion;
    }

    public void setSeleccion(boolean seleccion) {
        this.seleccion = seleccion;
    }
    // </editor-fold>
    
}
