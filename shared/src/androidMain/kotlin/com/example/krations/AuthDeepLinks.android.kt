package com.example.krations

import android.content.Intent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.core.context.GlobalContext

fun handleAuthDeepLink(intent: Intent?) {
    val uri = intent?.data ?: return
    if (
        intent.action == Intent.ACTION_VIEW &&
        uri.scheme == AUTH_SCHEME &&
        uri.host == AUTH_HOST
    ) {
        GlobalContext.get().get<SupabaseClient>().handleDeeplinks(intent)
    }
}

private const val AUTH_SCHEME = "com.example.krations"
private const val AUTH_HOST = "auth"
