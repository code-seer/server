package io.learnet.account.data.entity

import org.hibernate.search.engine.backend.types.Projectable
import org.hibernate.search.engine.backend.types.Sortable
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "social")
open class SocialEntity: Serializable {

        @Id
        @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne(mappedBy = "social")
        open var userProfile: UserProfileEntity? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "facebook", columnDefinition = "text")
        open var facebook: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "twitter", columnDefinition = "text")
        open var twitter: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "linkedin", columnDefinition = "text")
        open var linkedin: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "github", columnDefinition = "text")
        open var github: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "youtube", columnDefinition = "text")
        open var youtube: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "instagram", columnDefinition = "text")
        open var instagram: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "snapchat", columnDefinition = "text")
        open var snapchat: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "whatsapp", columnDefinition = "text")
        open var whatsapp: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "website", columnDefinition = "text")
        open var website: String? = null

        @Basic
        @GenericField(projectable = Projectable.YES)
        @Column(name = "other", columnDefinition = "text")
        open var other: String? = null

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
