package com.hinchmart.service;

import com.hinchmart.dto.request.UserAddressRequest;
import com.hinchmart.dto.response.UserAddressDto;
import com.hinchmart.entity.User;
import com.hinchmart.entity.UserAddress;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.repository.UserAddressRepository;
import com.hinchmart.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    public UserAddressService(UserAddressRepository userAddressRepository, UserRepository userRepository) {
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserAddressDto> getUserAddresses(Long userId) {
        return userAddressRepository.findByUserIdOrderByIsDefaultDescCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserAddressDto getAddressById(Long userId, Long addressId) {
        UserAddress address = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));
        return mapToDto(address);
    }

    @Transactional
    public UserAddressDto createAddress(Long userId, UserAddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // If setting as default, unset existing default
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            userAddressRepository.findByUserIdAndIsDefaultTrue(userId)
                    .ifPresent(existingDefault -> {
                        existingDefault.setDefault(false);
                        userAddressRepository.save(existingDefault);
                    });
        }

        UserAddress address = new UserAddress();
        address.setUser(user);
        applyRequestToAddress(address, request);

        // If it's the first address, make it default automatically
        if (userAddressRepository.findByUserIdOrderByIsDefaultDescCreatedAtDesc(userId).isEmpty()) {
            address.setDefault(true);
        }

        UserAddress saved = userAddressRepository.save(address);
        return mapToDto(saved);
    }

    @Transactional
    public UserAddressDto updateAddress(Long userId, Long addressId, UserAddressRequest request) {
        UserAddress address = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));

        if (Boolean.TRUE.equals(request.getIsDefault()) && !address.isDefault()) {
            userAddressRepository.findByUserIdAndIsDefaultTrue(userId)
                    .ifPresent(existingDefault -> {
                        existingDefault.setDefault(false);
                        userAddressRepository.save(existingDefault);
                    });
        }

        applyRequestToAddress(address, request);
        UserAddress saved = userAddressRepository.save(address);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));
        userAddressRepository.delete(address);
    }

    private void applyRequestToAddress(UserAddress address, UserAddressRequest request) {
        if (request.getLabel() != null) address.setLabel(request.getLabel());
        if (request.getFullName() != null) address.setFullName(request.getFullName());
        if (request.getPhone() != null) address.setPhone(request.getPhone());
        if (request.getAlternatePhone() != null) address.setAlternatePhone(request.getAlternatePhone());
        if (request.getCompanyName() != null) address.setCompanyName(request.getCompanyName());
        if (request.getGstin() != null) address.setGstin(request.getGstin());
        if (request.getAddressLine1() != null) address.setAddressLine1(request.getAddressLine1());
        if (request.getAddressLine2() != null) address.setAddressLine2(request.getAddressLine2());
        if (request.getCity() != null) address.setCity(request.getCity());
        if (request.getState() != null) address.setState(request.getState());
        if (request.getCountry() != null) address.setCountry(request.getCountry());
        if (request.getPostalCode() != null) address.setPostalCode(request.getPostalCode());
        if (request.getIsDefault() != null) address.setDefault(request.getIsDefault());
        if (request.getAddressType() != null) address.setAddressType(request.getAddressType());
        if (request.getSiteAccess() != null) address.setSiteAccess(request.getSiteAccess());
        if (request.getCraneAvailable() != null) address.setCraneAvailable(request.getCraneAvailable());
        if (request.getGatePassRequired() != null) address.setGatePassRequired(request.getGatePassRequired());
        if (request.getEntryTimings() != null) address.setEntryTimings(request.getEntryTimings());
    }

    public UserAddressDto mapToDto(UserAddress address) {
        UserAddressDto dto = new UserAddressDto();
        dto.setAddressId(address.getId());
        dto.setId("addr_" + address.getId());
        dto.setLabel(address.getLabel());
        dto.setFullName(address.getFullName());
        dto.setPhone(address.getPhone());
        dto.setAlternatePhone(address.getAlternatePhone());
        dto.setCompanyName(address.getCompanyName());
        dto.setGstin(address.getGstin());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());
        dto.setDefault(address.isDefault());
        dto.setAddressType(address.getAddressType());
        dto.setSiteAccess(address.getSiteAccess());
        dto.setCraneAvailable(address.isCraneAvailable());
        dto.setGatePassRequired(address.isGatePassRequired());
        dto.setEntryTimings(address.getEntryTimings());
        dto.setCreatedAt(address.getCreatedAt());
        return dto;
    }
}
