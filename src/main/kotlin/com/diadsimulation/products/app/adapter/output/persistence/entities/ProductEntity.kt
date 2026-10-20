package com.diadsimulation.products.app.adapter.output.persistence.entities

import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "products")
class ProductEntity(
    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),
    @Column(nullable = false, unique = true, length = 3)
    var code: String,
    @Column(nullable = false, length = 150)
    var name: String,
    @Column(nullable = false, length = 30)
    @Enumerated(STRING)
    var category: ProductCategory,
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    @Column(name = "minimum_quantity")
    var minimumQuantity: Int? = null,
    @Column(name = "pricing_type", nullable = false, length = 20)
    @Enumerated(STRING)
    var pricingType: PricingType,
    @Column(name = "shelf_life_days")
    var shelfLifeDays: Int? = null,
    @Column(name = "production_lead_time_min_days")
    var productionLeadTimeMinDays: Int? = null,
    @Column(name = "production_lead_time_max_days")
    var productionLeadTimeMaxDays: Int? = null,
    @Column(nullable = false)
    var active: Boolean = true,
    @OneToMany(
        mappedBy = "product",
        cascade = [ALL],
        orphanRemoval = true,
    )
    val prices: MutableList<ProductPriceEntity> = mutableListOf(),
    @OneToMany(
        mappedBy = "product",
        cascade = [ALL],
        orphanRemoval = true,
    )
    val fees: MutableList<ProductFeeEntity> = mutableListOf(),
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
