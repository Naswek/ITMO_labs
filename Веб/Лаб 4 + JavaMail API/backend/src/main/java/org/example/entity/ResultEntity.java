package org.example.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
@Table(
        name = "results",
        indexes = @Index(name = "idx_results_user_id", columnList = "user_id"))
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x_value", nullable = false)
    private Double x;

    @Column(name = "y_value", nullable = false)
    private Double y;

    @Column(name = "r_value", nullable = false)
    private Double r;

    @Column(name = "hit", nullable = false)
    private Boolean hit;

    @JsonIgnore // все пользователи видят все точки
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public ResultEntity() {}

    public ResultEntity(Double x, Double y, Double r, Boolean hit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }

    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }

    public Double getR() { return r; }
    public void setR(Double r) { this.r = r; }

    public Boolean getHit() { return hit; }
    public void setHit(Boolean hit) { this.hit = hit; }

    public UserEntity getUser() { return user; }
    public void setUserId(UserEntity user) { this.user = user; }
}