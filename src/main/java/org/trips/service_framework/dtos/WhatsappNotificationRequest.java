package org.trips.service_framework.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.trips.service_framework.clients.request.NotificationRequest;

import java.util.List;
import java.util.stream.Collectors;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class WhatsappNotificationRequest extends NotificationRequest {

    private Data data;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        @Builder.Default
        private String provider = "whatsapp";

        private List<Contact> to;

        private MessageData data;

        public static List<Contact> getContacts(List<String> phoneNumbers) {
            return phoneNumbers.stream()
                    .map(pNo -> Contact.builder().phoneNumber(pNo).build())
                    .collect(Collectors.toList());
        }

        public static MessageData getMessageData(String templateName, String languageCode, String mediaType, String mediaUrl, List<String> params) {
            return MessageData.builder().messageTemplate(MessageData.getMessageData(templateName, languageCode, mediaType, mediaUrl, params)).build();
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
        private static class MessageData {
            private Template messageTemplate;

            public static Template getMessageData(String templateName, String languageCode, String mediaType, String mediaUrl, List<String> params) {
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
            private static class Template {
                private String templateName;

                @Builder.Default
                private Language language = Language.builder().build();

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
                private static class TemplateData {
                    private TemplateHeader header;

                    private TemplateBody body;

                    public static TemplateHeader getTemplateHeader(String type, String mediaUrl) {
                        return TemplateHeader.builder().type(type).mediaUrl(mediaUrl).build();
                    }

                    public static TemplateBody getTemplateBody(List<String> params) {
                        return TemplateBody.builder().params(TemplateBody.getParams(params)).build();
                    }

                    @lombok.Data
                    @Builder
                    @NoArgsConstructor
                    @AllArgsConstructor
                    private static class  TemplateHeader {
                        private String type;

                        private String mediaUrl;
                    }

                    @lombok.Data
                    @Builder
                    @NoArgsConstructor
                    @AllArgsConstructor
                    private static class TemplateBody {
                        private List<Param> params;

                        public static List<Param> getParams(List<String> params) {
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
            private static class Language {
                @Builder.Default
                private String code = "en";
            }
        }
    }

}
