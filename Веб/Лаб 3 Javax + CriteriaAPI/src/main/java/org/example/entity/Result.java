package org.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "results")
public class Result  {

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

    public Result() {}

    public Result(Double x, Double y, Double r, Boolean hit) {
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

}