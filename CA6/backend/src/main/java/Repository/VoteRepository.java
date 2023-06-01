package Repository;

import Domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByCommentId(Integer commentId);
}
