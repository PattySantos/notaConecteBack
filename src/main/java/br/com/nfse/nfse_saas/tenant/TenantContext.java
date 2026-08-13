package br.com.nfse.nfse_saas.tenant;

public final class TenantContext {
    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();
    private TenantContext() { }
    public static void setTenantId(Long tenantId) { TENANT.set(tenantId); }
    public static Long getTenantId() {
        Long tenantId = TENANT.get();
        if (tenantId == null) throw new IllegalStateException("Tenant nao informado");
        return tenantId;
    }
    public static void clear() { TENANT.remove(); }
}
