package io.learnet.account.data.entity

import org.hibernate.annotations.Type
import org.hibernate.search.engine.backend.types.Projectable
import org.hibernate.search.engine.backend.types.Sortable
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "security")
open class SecurityEntity: Serializable {

        @Id
        @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne(mappedBy = "security")
        open var userProfile: UserProfileEntity? = null

        @FullTextField(analyzer = "english", projectable = Projectable.YES)
        @Type(type = "io.learnet.account.data.type.ArrayUserType")
        @Column(name = "roles", columnDefinition = "text[]")
        open var roles: Array<String> = emptyArray()

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
