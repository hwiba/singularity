package me.singularityfor.group.service;

import lombok.NonNull;
import me.singularityfor.group.domain.Group;
import me.singularityfor.group.repository.GroupRepository;
import me.singularityfor.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by hyva on 2015. 11. 14..
 */
@Service
@Transactional
public class GroupService {

    @Resource private GroupRepository groupRepository;

    public Group createGroup(@NonNull Group group) {
        return groupRepository.save(group);
    }
}
