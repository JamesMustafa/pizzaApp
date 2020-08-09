package bg.jamesmustafa.pizzaria.db.entity.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Date;

//can two classes be @MappedSuperclass?
@MappedSuperclass
public abstract class BaseDeletableEntity extends BaseEntity {

    @Column(name = "is_deleted" , nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private Date deletedOn;

    protected BaseDeletableEntity() {
    }

    @PrePersist
    public void setIsDeleted() {
        this.isDeleted = false;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }
}
