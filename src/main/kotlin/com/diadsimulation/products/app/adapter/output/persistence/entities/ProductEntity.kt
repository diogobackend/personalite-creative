package com.diadsimulation.products.app.adapter.output.persistence.entities

import com.diadsimulation.products.core.domain.enums.PricingType
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
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
    @Column(nullable = false, length = 150)
    var category: String,
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
    @ElementCollection
    @CollectionTable(name = "product_colors", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "color", nullable = false)
    val colors: MutableList<String> = mutableListOf(),
    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "size", nullable = false)
    val sizes: MutableList<String> = mutableListOf(),
    @ElementCollection
    @CollectionTable(name = "product_brands", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "brand", nullable = false)
    val brands: MutableList<String> = mutableListOf(),
    @ElementCollection
    @CollectionTable(name = "product_models", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "model", nullable = false)
    val models: MutableList<String> = mutableListOf(),
    @ElementCollection
    @CollectionTable(name = "product_materials", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "material", nullable = false)
    val materials: MutableList<String> = mutableListOf(),
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "category_id")
    val categoryId: UUID? = null,
)
