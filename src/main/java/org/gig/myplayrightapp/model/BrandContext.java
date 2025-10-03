package org.gig.myplayrightapp.model;

import org.gig.myplayrightapp.enums.Brand;

public final class BrandContext {
    private static final ThreadLocal<Brand> CURRENT = new ThreadLocal<>();
    public static void set(Brand brand) { CURRENT.set(brand); }
    public static Brand get() { return CURRENT.get(); }
    public static void clear() { CURRENT.remove(); }
    private BrandContext() {}
}