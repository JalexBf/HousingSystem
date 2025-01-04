package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.services.OwnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {
    OwnerServiceImpl ownerService;

    public OwnerController(OwnerServiceImpl ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("")
    public List<AppUser> findAllOwners(){
        return ownerService.findAllOwners();
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<AppUser> getOwner(@PathVariable Long ownerId) {
        Optional<AppUser> appUser = ownerService.getOwner(ownerId);
        return appUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

}

