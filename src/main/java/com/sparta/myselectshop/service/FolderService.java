package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {

        // 이미 있는 폴더 가져오기
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);

        // 새로 만드는 폴더
        List<Folder> folderList = new ArrayList<>();

        for (String folderName : folderNames) {
            // 받아온 폴더 이름과 이미 존재하는 폴더의 이름이 동일하지 않을 경우
            if (!isExistFolderName(folderName, existFolderList)) {
                folderList.add(new Folder(folderName, user));
            } else {
                throw new IllegalArgumentException("중복된 폴더명을 제거해주세요! 폴더명: " + folderName);
            }
        }

        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }

        return responseDtoList;
    }

    private boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        for (Folder existFolder : existFolderList) {
            return folderName.equals(existFolder.getName());
        }
        return false;
    }

}
