package bg.jamesmustafa.pizzaria.db.entity.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.Date;

//can two classes be @MappedSuperclass?
@MappedSuperclass
public abstract class BaseDeletableEntity extends BaseEntity {

    @Column(name = "is_deleted" , nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;

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

    public LocalDateTime getDeletedOn() { return deletedOn; }

    public void setDeletedOn(LocalDateTime deletedOn) { this.deletedOn = deletedOn; }
}
