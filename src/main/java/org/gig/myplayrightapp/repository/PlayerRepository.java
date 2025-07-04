package org.gig.myplayrightapp.repository;

import org.gig.myplayrightapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByAlias(String alias);

    Optional<Player> findByEmail(String email);

    Optional<Player> findByPhone(String phone);

    Optional<Player> findByNationalId(String nationalId);

    @Query("""
    SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
    FROM Player p
    WHERE p.alias = :alias
       OR p.phone = :phone
       OR p.nationalId = :nationalId
       OR p.email = :email
""")
    boolean existsByAny(String alias, String phone, String nationalId, String email);
}
