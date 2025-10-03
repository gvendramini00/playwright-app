package org.gig.myplayrightapp.service;

import org.gig.myplayrightapp.dto.InsertPlayerDTO;

import java.util.Optional;

public interface PlayerService {
    /**
     * Checks if a player with the given alias exists.
     */
    boolean existsByAlias(String alias);

    /**
     * Checks if a player with the given phone exists.
     */
    boolean existsByPhone(String phone);

    /**
     * Checks if a player with the given national ID exists.
     */
    boolean existsByNationalId(String nationalId);

    /**
     * Checks if a player with the given email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Checks whether any player exists in the database with a matching alias, phone number, national ID, or email.
     * <p>
     * This method is useful for validating uniqueness constraints during user registration to ensure
     * no duplicate players are created based on key identifying attributes.
     *
     * @param alias the player's chosen username (alias) to check for duplicates
     * @param phone the player's phone number to check for duplicates
     * @param nationalId the player's national identification number to check for duplicates
     * @param email the player's email address to check for duplicates
     * @return {@code true} if any player exists with the provided alias, phone, national ID, or email; {@code false} otherwise
     */
    boolean existsAnyMatching(String alias, String phone, String nationalId, String email);

    Optional<InsertPlayerDTO> findAnyExistingPlayer();


}
