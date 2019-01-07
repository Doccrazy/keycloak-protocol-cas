package org.keycloak.protocol.cas.mappers;

import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.ProtocolMapperUtils;
import org.keycloak.protocol.oidc.mappers.OIDCAttributeMapperHelper;
import org.keycloak.provider.ProviderConfigProperty;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserSessionMapper extends AbstractCASProtocolMapper {
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(ProtocolMapperUtils.USER_ATTRIBUTE);
        property.setLabel(ProtocolMapperUtils.USER_MODEL_PROPERTY_LABEL);
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText(ProtocolMapperUtils.USER_MODEL_PROPERTY_HELP_TEXT);
        configProperties.add(property);
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        OIDCAttributeMapperHelper.addJsonTypeConfig(configProperties);
    }

    public static final String PROVIDER_ID = "cas-usersession-property-mapper";


    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "User Session Model";
    }

    @Override
    public String getHelpText() {
        return "UserSessionModel property to a token claim.";
    }

    @Override
    public void setAttribute(Map<String, Object> attributes, ProtocolMapperModel mappingModel, UserSessionModel userSession) {
        String propertyName = mappingModel.getConfig().get(ProtocolMapperUtils.USER_ATTRIBUTE);
        String propertyValue = getUserSessionProperty(userSession, propertyName);
        setMappedAttribute(attributes, mappingModel, propertyValue);
    }

    public static ProtocolMapperModel create(String name, String userAttribute,
                                             String tokenClaimName, String claimType) {
        ProtocolMapperModel mapper = CASAttributeMapperHelper.createClaimMapper(name, tokenClaimName,
                claimType, PROVIDER_ID);
        mapper.getConfig().put(ProtocolMapperUtils.USER_ATTRIBUTE, userAttribute);
        return mapper;
    }


    public static String getUserSessionProperty(UserSessionModel userSessionModel, String propertyName) {
        String methodName = "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

        Method method;
        Object val;
        try {
            method = UserSessionModel.class.getMethod(methodName);
            val = method.invoke(userSessionModel);
            if (val != null) {
                return val.toString();
            }
        } catch (Exception var6) {
            ;
        }

        methodName = "is" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

        try {
            method = UserSessionModel.class.getMethod(methodName);
            val = method.invoke(userSessionModel);
            if (val != null) {
                return val.toString();
            }
        } catch (Exception var5) {
            ;
        }

        return null;
    }
}
