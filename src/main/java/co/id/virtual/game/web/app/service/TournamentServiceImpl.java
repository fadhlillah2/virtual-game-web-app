package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.tournament.TournamentDto;
import co.id.virtual.game.web.app.dto.tournament.TournamentParticipantDto;
import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.Tournament;
import co.id.virtual.game.web.app.model.TournamentParticipant;
import co.id.virtual.game.web.app.model.TournamentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TournamentServiceImpl implements TournamentService {
    @Override
    public Page<TournamentDto> getAllTournaments(Pageable pageable) {
        return null;
    }

    @Override
    public TournamentDto getTournamentById(UUID tournamentId) {
        return null;
    }

    @Override
    public List<TournamentDto> getTournamentsByStatus(TournamentStatus status) {
        return null;
    }

    @Override
    public List<TournamentDto> getTournamentsByGameType(GameType gameType) {
        return null;
    }

    @Override
    public List<TournamentDto> getUpcomingTournaments() {
        return null;
    }

    @Override
    public List<TournamentDto> getActiveTournaments() {
        return null;
    }

    @Override
    public Page<TournamentDto> getCompletedTournaments(Pageable pageable) {
        return null;
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        return null;
    }

    @Override
    public Tournament updateTournament(UUID tournamentId, Tournament tournament) {
        return null;
    }

    @Override
    public void deleteTournament(UUID tournamentId) {

    }

    @Override
    public TournamentParticipant registerForTournament(UUID tournamentId, UUID userId) {
        return null;
    }

    @Override
    public void unregisterFromTournament(UUID tournamentId, UUID userId) {

    }

    @Override
    public List<TournamentParticipantDto> getTournamentParticipants(UUID tournamentId) {
        return null;
    }

    @Override
    public List<TournamentParticipantDto> getTopTournamentParticipants(UUID tournamentId, int limit) {
        return null;
    }

    @Override
    public boolean isUserRegisteredForTournament(UUID tournamentId, UUID userId) {
        return false;
    }

    @Override
    public Page<TournamentDto> getUserTournamentHistory(UUID userId, Pageable pageable) {
        return null;
    }

    @Override
    public Tournament startTournament(UUID tournamentId) {
        return null;
    }

    @Override
    public Tournament endTournament(UUID tournamentId) {
        return null;
    }

    @Override
    public Tournament cancelTournament(UUID tournamentId) {
        return null;
    }

    @Override
    public TournamentParticipant updateParticipantScore(UUID tournamentId, UUID userId, long score) {
        return null;
    }

    @Override
    public TournamentParticipant eliminateParticipant(UUID tournamentId, UUID userId) {
        return null;
    }

    @Override
    public TournamentDto convertToDto(Tournament tournament, UUID currentUserId) {
        return null;
    }

    @Override
    public TournamentParticipantDto convertToDto(TournamentParticipant participant) {
        return null;
    }
}
