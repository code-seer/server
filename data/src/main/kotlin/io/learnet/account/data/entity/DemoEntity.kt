package io.learnet.account.data.entity

import org.hibernate.annotations.Type
import org.hibernate.search.engine.backend.types.Projectable
import org.hibernate.search.engine.backend.types.Sortable
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Indexed(index = "learnet-demo_user")
@Table(schema = "public", name = "demo_user")
open class DemoUserEntity: Serializable {

        @Id
        @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "firstName_raw", sortable = Sortable.YES)
        @Column(name = "first_name", columnDefinition = "text")
        open var firstName: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "lastName_raw", sortable = Sortable.YES)
        @Column(name = "last_name", columnDefinition = "text")
        open var lastName: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "city_raw", sortable = Sortable.YES)
        @Column(name = "city", columnDefinition = "text")
        open var city: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "state_raw", sortable = Sortable.YES)
        @Column(name = "state", columnDefinition = "text")
        open var state: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "zip_raw", sortable = Sortable.YES)
        @Column(name = "zip", columnDefinition = "text")
        open var zip: String? = null

        @Basic
        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @KeywordField(name = "address_raw", sortable = Sortable.YES)
        @Column(name = "address", columnDefinition = "text")
        open var address: String? = null

        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @Type(type = "io.learnet.account.data.type.ArrayUserType")
        @Column(name = "favorites", columnDefinition = "text[]")
        open var favorites: Array<String> = emptyArray()

        @Basic
        @FullTextField(analyzer = "keyword", projectable = Projectable.YES)
        @Column(name = "avatar")
        open var avatar: String? = null

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
