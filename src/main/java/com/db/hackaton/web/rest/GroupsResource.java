package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.service.GroupsService;
import com.db.hackaton.service.dto.GroupsDTO;
import com.db.hackaton.web.rest.util.HeaderUtil;
import com.db.hackaton.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Groups.
 */
@RestController
@RequestMapping("/api")
public class GroupsResource {

    private static final String ENTITY_NAME = "groups";
    private final Logger log = LoggerFactory.getLogger(GroupsResource.class);
    private final GroupsService groupsService;

    public GroupsResource(GroupsService groupsService) {
        this.groupsService = groupsService;
    }

    /**
     * POST  /groups : Create a new groups.
     *
     * @param groupsDTO the groupsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupsDTO, or with status 400 (Bad Request) if the groups has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups")
    @Timed
    public ResponseEntity<GroupsDTO> createGroups(@Valid @RequestBody GroupsDTO frontEndGroupDTO) throws URISyntaxException {
        log.debug("REST request to save Groups : {}", frontEndGroupDTO);
        if (frontEndGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "id-exists", "A new groups cannot already have an ID")).body(null);
        }
        for (GroupsDTO odsGroupDTO : groupsService.findAll()) {
            if (odsGroupDTO.getName().toUpperCase().equals(frontEndGroupDTO.getName().toUpperCase())) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "name-exists", "A group with same name already exists.")).body(null);
            }
        }
        GroupsDTO result = groupsService.save(frontEndGroupDTO);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups : Updates an existing groups.
     *
     * @param groupsDTO the groupsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupsDTO,
     * or with status 400 (Bad Request) if the groupsDTO is not valid,
     * or with status 500 (Internal Server Error) if the groupsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups")
    @Timed
    public ResponseEntity<GroupsDTO> updateGroups(@Valid @RequestBody GroupsDTO groupsDTO) throws URISyntaxException {
        log.debug("REST request to update Groups : {}", groupsDTO);
        if (groupsDTO.getId() == null) {
            return createGroups(groupsDTO);
        }
        GroupsDTO result = groupsService.save(groupsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groupsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/groups")
    @Timed
    public ResponseEntity<List<GroupsDTO>> getAllGroups(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Groups");
        Page<GroupsDTO> page = groupsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /groups/:id : get the "id" groups.
     *
     * @param id the id of the groupsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/groups/{id}")
    @Timed
    public ResponseEntity<GroupsDTO> getGroups(@PathVariable Long id) {
        log.debug("REST request to get Groups : {}", id);
        GroupsDTO groupsDTO = groupsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupsDTO));
    }

    /**
     * DELETE  /groups/:id : delete the "id" groups.
     *
     * @param id the id of the groupsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroups(@PathVariable Long id) {
        log.debug("REST request to delete Groups : {}", id);
        groupsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/groups?query=:query : search for the groups corresponding
     * to the query.
     *
     * @param query    the query of the groups search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/groups")
    @Timed
    public ResponseEntity<List<GroupsDTO>> searchGroups(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Groups for query {}", query);
        Page<GroupsDTO> page = groupsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
