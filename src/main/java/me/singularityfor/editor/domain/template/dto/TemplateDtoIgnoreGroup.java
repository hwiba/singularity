package me.singularityfor.editor.domain.template.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.group.domain.Group;
import me.singularityfor.user.domain.User;

/**
 * Created by hyva on 2015. 11. 14..
 */
public class TemplateDtoIgnoreGroup extends Template {

    @Getter
    @JsonIgnore private Group group;

    public TemplateDtoIgnoreGroup(Template template) {
        super(template.getAuthor(), template.getGroup(), template.getName(), template.getForm());
        this.group = template.getGroup();
    }
}
