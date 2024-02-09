package personal.projects.messagingapp.messagelist;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Setter
@Getter
@Table(value = "messages_by_user_folder")
public class MessageListItem {

    @PrimaryKey
    private MessageListItemKey key;
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> to;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String from;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String subject;
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean isUnread;
    @Transient
    private String agoTimeString;
}
