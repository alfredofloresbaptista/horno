package models;

import io.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Common part of entities representation.
 * Used as "table per entity" skeleton.
 */
@MappedSuperclass
public abstract class NamedModel extends Model {

//    @JsonView({NamedViews.Public.class, ActionsView.CreateView.class})
    @Id
    public Integer id;

//    @JsonView({NamedViews.Internal.class, ActionsView.CreateView.class})
//    @JsonSerialize(using = OffsetDateTimeSerializer.class)
//    @WhenCreated
//    public DateTimeException whenCreated;
//
//    @JsonView({NamedViews.Internal.class, ActionsView.UpdateView.class})
//    @JsonSerialize(using = OffsetDateTimeSerializer.class)
//    @WhenModified
//    public OffsetDateTime whenUpdated;

//    @JsonView(NamedViews.Internal.class)
    @Version
    public long version;

//    @JsonView(NamedViews.Internal.class)
//    @JsonSerialize(using = StateSerializer.class)
//    @SoftDelete
//    public boolean state;
}
