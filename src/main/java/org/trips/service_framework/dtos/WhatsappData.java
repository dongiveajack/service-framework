package org.trips.service_framework.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappData {
    @Builder.Default
    private String provider = "whatsapp";

    private List<Contact> to;

    private Data data;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        private String phoneNumber;
    }

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Template messageTemplate;

        @lombok.Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Template {
            private String templateName;

            private Language language;

            private TemplateData richTemplateData;

            @lombok.Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class TemplateData {
                private TemplateHeader header;

                private TemplateBody body;


                @lombok.Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                public static class  TemplateHeader {
                    private String type;

                    private String mediaUrl;
                }

                @lombok.Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                public static class TemplateBody {
                    private List<Param> params;

                    @lombok.Data
                    @Builder
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class Param {
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
            @Builder.Default
            private String code = "en";
        }
    }


}
