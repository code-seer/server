package io.learnet.account.data.entity

import org.hibernate.search.engine.backend.types.Projectable
import org.hibernate.search.engine.backend.types.Sortable
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "address")
open class AddressEntity: Serializable {

        @Id
        @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne(mappedBy = "address")
        open var userProfile: UserProfileEntity? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "country_raw", sortable = Sortable.YES)
        @Column(name = "country", columnDefinition = "text")
        open var country: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "state_raw", sortable = Sortable.YES)
        @Column(name = "state", columnDefinition = "text")
        open var state: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "city_raw", sortable = Sortable.YES)
        @Column(name = "city", columnDefinition = "text")
        open var city: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "postalCode_raw", sortable = Sortable.YES)
        @Column(name = "postal_code", columnDefinition = "text")
        open var postalCode: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "address1_raw", sortable = Sortable.YES)
        @Column(name = "address_1", columnDefinition = "text")
        open var address1: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "address2_raw", sortable = Sortable.YES)
        @Column(name = "address_2", columnDefinition = "text")
        open var address2: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
        @Column(name = "created_dt", columnDefinition = "timestamptz")
        open var createdDt: OffsetDateTime = OffsetDateTime.now()

        @Basic
        @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
        @Column(name = "updated_dt", columnDefinition = "timestamptz")
        open var updatedDt: OffsetDateTime = OffsetDateTime.now()

        @Basic
        @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
        @Column(name = "_uuid", columnDefinition = "uuid")
        open var uuid: UUID? = null
}
