package com.khokhlov.tendermonitoring.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tenders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(columnDefinition = "TEXT")
    private String procurementLaws;

    @Column(columnDefinition = "TEXT")
    private String stage;

    @Column(columnDefinition = "TEXT")
    private String purchaseObject;

    private BigDecimal price;

    private String currency;

    private LocalDateTime postedDate;
    private LocalDateTime updatedDate;
    private LocalDateTime expirationDate;

    @Column(columnDefinition = "TEXT")
    private String purchaseCode;

    @Column(columnDefinition = "TEXT")
    private String author;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Tender tender = (Tender) o;
        return getId() != null && Objects.equals(getId(), tender.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
