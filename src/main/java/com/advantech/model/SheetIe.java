package com.advantech.model;
// Generated 2017/3/27 上午 08:57:09 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SheetIe generated by hbm2java
 */
@Entity
@Table(name = "Sheet_IE",
        schema = "dbo",
        catalog = "E_Document"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SheetIe implements java.io.Serializable {

    private int id;
    private Model model;
    private BigDecimal totalModule;
    private Integer assyStations;
    private Integer packingStations;
    private BigDecimal assyLeadTime;
    private BigDecimal assyKanbanTime;
    private BigDecimal packingLeadTime;
    private BigDecimal packingKanbanTime;
    private Date modifiedDate;

    public SheetIe() {
    }

    public SheetIe(int id) {
        this.id = id;
    }

    public SheetIe(int id, Model model, BigDecimal totalModule, Integer assyStations, Integer packingStations, BigDecimal assyLeadTime, BigDecimal assyKanbanTime, BigDecimal packingLeadTime, BigDecimal packingKanbanTime, Date modifiedDate) {
        this.id = id;
        this.model = model;
        this.totalModule = totalModule;
        this.assyStations = assyStations;
        this.packingStations = packingStations;
        this.assyLeadTime = assyLeadTime;
        this.assyKanbanTime = assyKanbanTime;
        this.packingLeadTime = packingLeadTime;
        this.packingKanbanTime = packingKanbanTime;
        this.modifiedDate = modifiedDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Model_id")
    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Column(name = "Total_Module", precision = 10, scale = 1)
    public BigDecimal getTotalModule() {
        return this.totalModule;
    }

    public void setTotalModule(BigDecimal totalModule) {
        this.totalModule = totalModule;
    }

    @Column(name = "ASSY_stations")
    public Integer getAssyStations() {
        return this.assyStations;
    }

    public void setAssyStations(Integer assyStations) {
        this.assyStations = assyStations;
    }

    @Column(name = "Packing_stations")
    public Integer getPackingStations() {
        return this.packingStations;
    }

    public void setPackingStations(Integer packingStations) {
        this.packingStations = packingStations;
    }

    @Column(name = "ASSY_lead_time", precision = 10, scale = 1)
    public BigDecimal getAssyLeadTime() {
        return this.assyLeadTime;
    }

    public void setAssyLeadTime(BigDecimal assyLeadTime) {
        this.assyLeadTime = assyLeadTime;
    }

    @Column(name = "ASSY_kanban_time", precision = 10, scale = 1)
    public BigDecimal getAssyKanbanTime() {
        return this.assyKanbanTime;
    }

    public void setAssyKanbanTime(BigDecimal assyKanbanTime) {
        this.assyKanbanTime = assyKanbanTime;
    }

    @Column(name = "Packing_lead_time", precision = 10, scale = 1)
    public BigDecimal getPackingLeadTime() {
        return this.packingLeadTime;
    }

    public void setPackingLeadTime(BigDecimal packingLeadTime) {
        this.packingLeadTime = packingLeadTime;
    }

    @Column(name = "Packing_kanban_time", precision = 10, scale = 1)
    public BigDecimal getPackingKanbanTime() {
        return this.packingKanbanTime;
    }

    public void setPackingKanbanTime(BigDecimal packingKanbanTime) {
        this.packingKanbanTime = packingKanbanTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Modified_Date", length = 23, updatable = false)
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.model);
        hash = 43 * hash + Objects.hashCode(this.totalModule);
        hash = 43 * hash + Objects.hashCode(this.assyStations);
        hash = 43 * hash + Objects.hashCode(this.packingStations);
        hash = 43 * hash + Objects.hashCode(this.assyLeadTime);
        hash = 43 * hash + Objects.hashCode(this.assyKanbanTime);
        hash = 43 * hash + Objects.hashCode(this.packingLeadTime);
        hash = 43 * hash + Objects.hashCode(this.packingKanbanTime);
        hash = 43 * hash + Objects.hashCode(this.modifiedDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SheetIe other = (SheetIe) obj;
        if (!Objects.equals(this.model, other.model)) {
            return false;
        }
        if (!Objects.equals(this.totalModule, other.totalModule)) {
            return false;
        }
        if (!Objects.equals(this.assyStations, other.assyStations)) {
            return false;
        }
        if (!Objects.equals(this.packingStations, other.packingStations)) {
            return false;
        }
        if (!Objects.equals(this.assyLeadTime, other.assyLeadTime)) {
            return false;
        }
        if (!Objects.equals(this.assyKanbanTime, other.assyKanbanTime)) {
            return false;
        }
        if (!Objects.equals(this.packingLeadTime, other.packingLeadTime)) {
            return false;
        }
        if (!Objects.equals(this.packingKanbanTime, other.packingKanbanTime)) {
            return false;
        }
        if (!Objects.equals(this.modifiedDate, other.modifiedDate)) {
            return false;
        }
        return true;
    }

}
