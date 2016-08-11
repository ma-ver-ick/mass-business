package de.markusseidl.massbusiness.presentation;


import de.markusseidl.massbusiness.business.service.ControlService;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * ControlBean
 * <p>
 * Created on 24.07.2016
 */
@SessionScoped
@ManagedBean
public class ControlBean implements Serializable {

    private final static Logger LOG = Logger.getLogger(ControlBean.class);

    @EJB
    private ControlService _service;

    public void createData() {
        _service.createStorageParts();
    }

    public void runExample01() { _service.runExample01(); }
}
