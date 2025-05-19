package org.trips.service_framework.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.trips.service_framework.clients.request.NotificationRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * contains the data required for the sending a whatsapp notification.
 */
@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WhatsappNotificationRequest extends NotificationRequest {
    private Data data;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String provider;

        private List<Contact> to;

        private MessageData data;

        public static List<Contact> getContacts(List<String> phoneNumbers) {
            if (Objects.isNull(phoneNumbers)) {
                return null;
            }
            return phoneNumbers.stream()
                    .map(pNo -> Contact.builder().phoneNumber(pNo).build())
                    .collect(Collectors.toList());
        }

        public static MessageData getMessageData(String templateName, String languageCode, String mediaType, String mediaUrl, List<String> params) {
            return MessageData.builder().messageTemplate(MessageData.getTemplate(templateName, languageCode, mediaType, mediaUrl, params)).build();
        }

        @lombok.Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        private static class Contact {
            private String phoneNumber;
        }

        @lombok.Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MessageData {
            private Template messageTemplate;

            public static Template getTemplate(String templateName, String languageCode, String mediaType, String mediaUrl, List<String> params) {
                return Template.builder()
                        .templateName(templateName)
                        .language(Template.getLanguage(languageCode))
                        .richTemplateData(Template.getTemplateData(mediaType, mediaUrl, params))
                        .build();
            }

            @lombok.Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Template {
                private String templateName;

                private Language language;

                private TemplateData richTemplateData;

                private static Language getLanguage(String languageCode) {
                    return Language.builder().code(languageCode).build();
                }

                public static TemplateData getTemplateData(String mediaType, String mediaUrl, List<String> params) {
                    return TemplateData.builder()
                            .header(TemplateData.getTemplateHeader(mediaType, mediaUrl))
                            .body(TemplateData.getTemplateBody(params))
                            .build();
                }

                @lombok.Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                public static class TemplateData {
                    private TemplateHeader header;

                    private TemplateBody body;

                    public static TemplateHeader getTemplateHeader(String type, String mediaUrl) {
                        if (Objects.isNull(type)) {
                            return null;
                        }
                        return TemplateHeader.builder().type(type).mediaUrl(mediaUrl).build();
                    }

                    public static TemplateBody getTemplateBody(List<String> params) {
                        return TemplateBody.builder().params(TemplateBody.getParams(params)).build();
                    }

                    @lombok.Data
                    @Builder
                    @NoArgsConstructor
                    @AllArgsConstructor
                    private static class TemplateHeader {
                        private String type;

                        private String mediaUrl;
                    }

                    @lombok.Data
                    @Builder
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class TemplateBody {
                        private List<Param> params;

                        public static List<Param> getParams(List<String> params) {
                            if (Objects.isNull(params)) {
                                return null;
                            }
                            return params.stream()
                                    .map(p -> Param.builder().data(p).build())
                                    .collect(Collectors.toList());
                        }

                        @lombok.Data
                        @Builder
                        @NoArgsConstructor
                        @AllArgsConstructor
                        private static class Param {
                            private String data;
                        }
                    }

                }
            }

            @lombok.Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Language {
                private String code;
            }
        }
    }

}
