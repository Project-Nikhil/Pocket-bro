import React from 'react';
import { Shield, Database, Lock, EyeOff, Wifi, UserCheck } from 'lucide-react';

export const PrivacySection: React.FC = () => {
  return (
    <section id="privacy" className="section" style={{ backgroundColor: 'var(--bg-secondary)' }}>
      <div className="container">
        
        {/* Section Header */}
        <div className="section-header">
          <div className="pill-badge" style={{ marginBottom: '14px' }}>
            <Lock size={14} color="#F5C58A" />
            <span>Honest & Verified Architecture</span>
          </div>
          <h2>Made to feel personal. Designed with privacy in mind.</h2>
          <p>
            Your sisterly chats, vulnerable thoughts, and daily calendar belong to you and your phone alone.
          </p>
        </div>

        {/* 4 Architectural Privacy Pillars */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))',
          gap: '16px',
          marginBottom: '32px',
        }}>
          {/* Pillar 1: Local SQLite Room Database */}
          <div className="glass-card" style={{ padding: '22px 18px' }}>
            <div style={{
              width: '40px',
              height: '40px',
              borderRadius: '12px',
              background: 'rgba(96, 165, 250, 0.12)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: '14px',
            }}>
              <Database size={20} color="#60A5FA" />
            </div>
            <h3 style={{ fontSize: '1.05rem', color: 'var(--text-cream)', marginBottom: '8px', fontWeight: 600 }}>
              100% Local SQLite Storage
            </h3>
            <p style={{ fontSize: '0.865rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              All chat history, diary notes, reminders, calendar entries, and voice recordings are stored locally in the on-device Room SQLite database (<code>pocket_brother_db</code>). Nothing is synced to an external database.
            </p>
          </div>

          {/* Pillar 2: Built-in Instant Offline Core */}
          <div className="glass-card" style={{ padding: '22px 18px' }}>
            <div style={{
              width: '40px',
              height: '40px',
              borderRadius: '12px',
              background: 'rgba(245, 197, 138, 0.12)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: '14px',
            }}>
              <Shield size={20} color="var(--accent-amber)" />
            </div>
            <h3 style={{ fontSize: '1.05rem', color: 'var(--text-cream)', marginBottom: '8px', fontWeight: 600 }}>
              On-Device Rule Engine
            </h3>
            <p style={{ fontSize: '0.865rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              The core conversational logic and intent parser runs entirely offline on your phone's processor. It requires no network requests, no remote servers, and zero background analytics to respond.
            </p>
          </div>

          {/* Pillar 3: Gemma On-Device Local LLM */}
          <div className="glass-card" style={{ padding: '22px 18px' }}>
            <div style={{
              width: '40px',
              height: '40px',
              borderRadius: '12px',
              background: 'rgba(142, 167, 233, 0.12)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: '14px',
            }}>
              <EyeOff size={20} color="#8EA7E9" />
            </div>
            <h3 style={{ fontSize: '1.05rem', color: 'var(--text-cream)', marginBottom: '8px', fontWeight: 600 }}>
              On-Device Gemma LLM
            </h3>
            <p style={{ fontSize: '0.865rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              Pocket Brother supports on-device quantized Gemma LLM models (Nano 2GB, Balanced 4GB, and Pro 8GB). Model weights are kept directly in internal storage and execute on your phone's CPU/GPU.
            </p>
          </div>

          {/* Pillar 4: Zero Sign-Up or Tracking */}
          <div className="glass-card" style={{ padding: '22px 18px' }}>
            <div style={{
              width: '40px',
              height: '40px',
              borderRadius: '12px',
              background: 'rgba(52, 211, 153, 0.12)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: '14px',
            }}>
              <UserCheck size={20} color="#34D399" />
            </div>
            <h3 style={{ fontSize: '1.05rem', color: 'var(--text-cream)', marginBottom: '8px', fontWeight: 600 }}>
              No Account or Telemetry
            </h3>
            <p style={{ fontSize: '0.865rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              Neither this website nor the mobile app collects personal contact details, email addresses, phone numbers, or analytics. You download the APK, install it, and start chatting immediately.
            </p>
          </div>
        </div>

        {/* Transparent Connectivity Note */}
        <div style={{
          background: 'rgba(20, 17, 15, 0.7)',
          border: '1px solid var(--border-medium)',
          borderRadius: 'var(--radius-lg)',
          padding: '20px 16px',
          display: 'flex',
          gap: '14px',
          alignItems: 'flex-start',
        }}>
          <Wifi size={20} color="var(--accent-amber)" style={{ flexShrink: 0, marginTop: '2px' }} />
          <div>
            <h4 style={{ fontSize: '0.975rem', color: 'var(--text-cream)', marginBottom: '6px', fontWeight: 600 }}>
              When is an internet connection needed?
            </h4>
            <p style={{ fontSize: '0.865rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              The standard app runs 100% offline using its built-in engine. An active internet connection is only required if:
              (1) You choose to download an optional multi-gigabyte Gemma model weight from within the app settings, or
              (2) You configure a remote server-side Gemini API key in the project's build settings. In that case, conversational prompts are processed securely between your phone and the configured AI provider.
            </p>
          </div>
        </div>

      </div>
    </section>
  );
};
