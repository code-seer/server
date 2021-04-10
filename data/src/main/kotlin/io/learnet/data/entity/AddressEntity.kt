package io.learnet.data.entity

import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "address")
open class AddressEntity: Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne(mappedBy = "address")
        open var userProfile: UserProfileEntity? = null

        @Basic
        @Column(name = "country", columnDefinition = "text")
        open var country: String? = null

        @Basic
        @Column(name = "state", columnDefinition = "text")
        open var state: String? = null

        @Basic
        @Column(name = "city", columnDefinition = "text")
        open var city: String? = null

        @Basic
        @Column(name = "postal_code", columnDefinition = "text")
        open var postalCode: String? = null

        @Basic
        @Column(name = "address_1", columnDefinition = "text")
        open var address1: String? = null

        @Basic
        @Column(name = "address_2", columnDefinition = "text")
        open var address2: String? = null

        @Basic
        @Column(name = "created_dt", columnDefinition = "timestamptz")
        open var createdDt: OffsetDateTime = OffsetDateTime.now()

        @Basic
        @Column(name = "updated_dt", columnDefinition = "timestamptz")
        open var updatedDt: OffsetDateTime = OffsetDateTime.now()

        @Basic
        @Column(name = "_uuid", columnDefinition = "uuid")
        open var uuid: UUID? = null
}
