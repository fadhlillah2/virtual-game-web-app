package co.id.virtual.game.web.app.model;

/**
 * Enum representing the status of a tournament.
 */
public enum TournamentStatus {
    /**
     * The tournament is open for registration.
     */
    REGISTRATION_OPEN,
    
    /**
     * The tournament registration is closed, but the tournament has not started yet.
     */
    REGISTRATION_CLOSED,
    
    /**
     * The tournament is in progress.
     */
    IN_PROGRESS,
    
    /**
     * The tournament has been completed.
     */
    COMPLETED,
    
    /**
     * The tournament has been cancelled.
     */
    CANCELLED
}
