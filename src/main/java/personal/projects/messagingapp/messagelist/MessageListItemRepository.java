package personal.projects.messagingapp.messagelist;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageListItemRepository extends CassandraRepository<MessageListItem, MessageListItemKey> {

    List<MessageListItem> findAllByKey_IdAndKey_Label(String id, String label);
}
