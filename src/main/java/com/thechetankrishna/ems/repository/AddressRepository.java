package com.thechetankrishna.ems.repository;

import com.thechetankrishna.ems.dto.AddressDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressDTO, String> {
}
