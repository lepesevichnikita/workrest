package org.klaster.domain.dto;

/*
 * workrest
 *
 * 13.03.2020
 *
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Transient;
import org.klaster.domain.builder.concrete.DefaultJobMessageBuilder;
import org.klaster.domain.builder.general.JobMessageBuilder;
import org.klaster.domain.model.entity.JobMessage;

/**
 * JobMessageDTO
 *
 * @author Nikita Lepesevich
 */
public class JobMessageDTO {

  @Transient
  @JsonIgnore
  private JobMessageBuilder defaultJobMessageBuilder;

  private String text;

  public JobMessageDTO() {
    defaultJobMessageBuilder = new DefaultJobMessageBuilder();
  }


  public static JobMessageDTO fromJobMessage(JobMessage jobMessage) {
    JobMessageDTO jobMessageDTO = new JobMessageDTO();
    jobMessageDTO.text = jobMessage.getText();
    return jobMessageDTO;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
