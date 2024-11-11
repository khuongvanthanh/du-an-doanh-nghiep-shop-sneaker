package com.example.service.impl;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.dto.requests.productRequests.ColorRequest;
import com.example.dto.response.productResponse.ColorResponse;
import com.example.exception.EntityNotFoundException;
import com.example.exception.NotAllowedDeleteEntityException;
import com.example.model.Color;
import com.example.model.User;
import com.example.repositories.products.ColorRepository;
import com.example.repositories.auth.UserRepository;
import com.example.service.ColorService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    private final UserRepository userRepository;

    @Override
    public List<ColorResponse> getAllColors(String keyword) {
        return this.colorRepository.findColorsByNameAndIsDeletedIsFalse(keyword).stream().map(this::convertToResponse).toList();
    }

    @Override
    public int storeColor(ColorRequest req) {
        if (this.colorRepository.existsColorByName(req.getName().trim())) {
            throw new EntityExistsException("Màu này " + req.getName() + " đã tồn tại trong hệ thống");
        }
        User user = getUserById(req.getUserId());
        Color color = new Color();
        color.setName(req.getName());
        color.setHexColorCode(req.getHex_code());
        color.setCreatedBy(user);
        color.setUpdatedBy(user);
        return this.colorRepository.save(color).getId();
    }

    @Override
    public void updateColor(ColorRequest req, int id) {
        User user = getUserById(req.getUserId());
        Color color = getColorById(id);
        if (!Objects.equals(req.getName(), color.getName().trim())) {
            if (this.colorRepository.existsColorByName(req.getName().trim())) {
                throw new EntityExistsException("Màu này " + req.getName() + " đã tồn tại trong hệ thống");
            }
        }
        color.setName(req.getName());
        color.setHexColorCode(req.getHex_code());
        color.setUpdatedBy(user);
        this.colorRepository.save(color);
    }

    @Override
    public void isDeleteColor(int id) {
        Color color = getColorById(id);
        try {
            this.colorRepository.delete(color);
        } catch (Exception e) {
            throw new NotAllowedDeleteEntityException("Không thể xóa màu này!");
        }
    }

    private Color getColorById(int id) {
        return this.colorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Color not found"));
    }

    private User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private ColorResponse convertToResponse(Color color) {
        return ColorResponse.builder()
                .id(color.getId())
                .name(color.getName())
                .hex_code(color.getHexColorCode())
                .createdBy(color.getCreatedBy().getUsername())
                .createdAt(color.getCreateAt())
                .updatedAt(color.getUpdateAt())
                .build();
    }
}
