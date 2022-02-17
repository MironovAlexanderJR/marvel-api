package ru.mironov.marvelapi.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mironov.marvelapi.domain.entity.Character;
import ru.mironov.marvelapi.domain.entity.Comic;
import ru.mironov.marvelapi.domain.entity.Creator;
import ru.mironov.marvelapi.domain.exception.creator.CreatorNotFoundException;
import ru.mironov.marvelapi.domain.mapper.CreatorMapper;
import ru.mironov.marvelapi.repository.CreatorRepository;
import ru.mironov.marvelapi.service.CharacterService;
import ru.mironov.marvelapi.service.ComicService;
import ru.mironov.marvelapi.service.CreatorService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author mironovAlexanderJR
 * @since 27.01.2022
 */
@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepository creatorRepository;
    private final CreatorMapper creatorMapper;
    private final ComicService comicService;
    private final CharacterService characterService;

    @Override
    public List<Creator> getAllCreator() {
        return creatorRepository.findAll();
    }

    @Override
    public Creator getCreator(UUID creatorId) {
        return creatorRepository.findById(creatorId)
                .orElseThrow(() -> new CreatorNotFoundException(creatorId));
    }

    @Override
    @Transactional
    public Creator createCreator(Creator creatorJson) {
        return creatorRepository.save(creatorJson);
    }

    @Override
    @Transactional
    public Creator updateCreator(UUID creatorId, Creator creatorJson) {
        return Optional.of(creatorId)
                .map(this::getCreator)
                .map(current -> creatorMapper.merge(current, creatorJson))
                .map(creatorRepository::save)
                .orElseThrow(() -> new CreatorNotFoundException(creatorId));
    }

    @Override
    @Transactional
    public void deleteCreator(UUID creatorId) {
        creatorRepository.deleteById(creatorId);
    }

    @Override
    @Transactional
    public Comic assignComic(UUID creatorId, Comic createDto) {
        creatorRepository.getById(creatorId).addComic(createDto);
        return createDto;
    }

    @Override
    @Transactional
    public Comic updateComic(UUID creatorsId, UUID comicId, Comic comicUpdateDto) {
        return comicService.updateComic(comicId, comicUpdateDto);

    }

    @Override
    @Transactional
    public void deleteComic(UUID creatorsId, UUID comicId) {
        comicService.deleteComic(comicId);
    }

    @Override
    @Transactional
    public Character assignCharacter(UUID creatorId, Character characterCreateDto) {
        creatorRepository.getById(creatorId).addCharacter(characterCreateDto);
        return characterCreateDto;
    }

    @Override
    @Transactional
    public Character updateCharacter(UUID creatorsId, UUID characterId, Character characterUpdateDto) {
        return characterService.updateCharacter(characterId, characterUpdateDto);
    }

    @Override
    @Transactional
    public void deleteCharacter(UUID creatorsId, UUID characterId) {
        characterService.deleteCharacter(characterId);
    }
}
