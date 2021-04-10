package io.learnet.data.entity

import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "social")
open class SocialEntity: Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne(mappedBy = "social")
        open var userProfile: UserProfileEntity? = null

        @Basic
        @Column(name = "facebook", columnDefinition = "text")
        open var facebook: String? = null

        @Basic
        @Column(name = "twitter", columnDefinition = "text")
        open var twitter: String? = null

        @Basic
        @Column(name = "linkedin", columnDefinition = "text")
        open var linkedin: String? = null

        @Basic
        @Column(name = "github", columnDefinition = "text")
        open var github: String? = null

        @Basic
        @Column(name = "youtube", columnDefinition = "text")
        open var youtube: String? = null

        @Basic
        @Column(name = "instagram", columnDefinition = "text")
        open var instagram: String? = null

        @Basic
        @Column(name = "snapchat", columnDefinition = "text")
        open var snapchat: String? = null

        @Basic
        @Column(name = "whatsapp", columnDefinition = "text")
        open var whatsapp: String? = null

        @Basic
        @Column(name = "website", columnDefinition = "text")
        open var website: String? = null

        @Basic
        @Column(name = "other", columnDefinition = "text")
        open var other: String? = null

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
