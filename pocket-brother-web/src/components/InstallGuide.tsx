import React, { useState } from 'react';
import { Download, FolderOpen, ShieldCheck, AlertTriangle, ChevronDown, ChevronUp, Smartphone, HardDrive, RefreshCw } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

export const InstallGuide: React.FC = () => {
  const [activeTroubleshoot, setActiveTroubleshoot] = useState<number | null>(null);

  const toggleTroubleshoot = (index: number) => {
    setActiveTroubleshoot(activeTroubleshoot === index ? null : index);
  };

  const troubleshootingItems = [
    {
      title: 'Installation failed: "App not installed"',
      icon: HardDrive,
      content:
        'This usually happens if your phone has insufficient storage space or if an older version with a conflicting signature is already present. Free up at least 150 MB of device storage. If you had an earlier test build, uninstall it before installing the fresh APK.',
    },
    {
      title: 'Play Protect prompt: "Unrecognized app / More details"',
      icon: ShieldCheck,
      content:
        'Because Pocket Brother is distributed directly as a personal APK rather than published on Google Play, Google Play Protect will flag it as an unknown developer. You can tap "More details" and choose "Install anyway" to proceed. You do NOT need to disable Play Protect on your phone.',
    },
    {
      title: 'Compatibility: "App is not compatible with your phone"',
      icon: Smartphone,
      content:
        'Pocket Brother requires Android 7.0 (Nougat, API level 24) or higher. Check your phone settings (Settings > About Phone > Android Version) to confirm your Android version meets the requirement.',
    },
    {
      title: 'How do I update when a new build is released?',
      icon: RefreshCw,
      content:
        'When your brother shares an updated APK, download and tap it. Android will ask: "Do you want to update this app? Your existing data will not be lost." Tap "Update". All your local chats, notes, and calendar events will remain intact.',
    },
  ];

  return (
    <section id="install" className="section">
      <div className="container">
        
        {/* Section Header */}
        <div className="section-header">
          <div className="pill-badge" style={{ marginBottom: '14px' }}>
            <Smartphone size={14} color="#F5C58A" />
            <span>Direct Sideload Guide</span>
          </div>
          <h2>Simple 3-Step Installation</h2>
          <p>
            Installing an Android APK directly is quick and safe. Follow these three steps on your phone.
          </p>
        </div>

        {/* 3 Steps Cards */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
          gap: '24px',
          marginBottom: '40px',
        }}>
          {/* Step 1 */}
          <div className="glass-card" style={{ padding: '30px 24px', position: 'relative' }}>
            <div style={{
              width: '42px',
              height: '42px',
              borderRadius: '12px',
              background: 'rgba(245, 197, 138, 0.1)',
              border: '1px solid rgba(245, 197, 138, 0.25)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--accent-amber)',
              fontWeight: 700,
              fontSize: '1.1rem',
              marginBottom: '20px',
            }}>
              1
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
              <Download size={18} color="var(--accent-amber)" />
              <h3 style={{ fontSize: '1.2rem', color: 'var(--text-cream)' }}>Download the APK</h3>
            </div>
            <p style={{ fontSize: '0.925rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              Tap <strong style={{ color: 'var(--text-cream)' }}>“Download for Android”</strong> on this website from your Android phone's browser (Chrome, Samsung Internet, etc.).
            </p>
          </div>

          {/* Step 2 */}
          <div className="glass-card" style={{ padding: '30px 24px', position: 'relative' }}>
            <div style={{
              width: '42px',
              height: '42px',
              borderRadius: '12px',
              background: 'rgba(245, 197, 138, 0.1)',
              border: '1px solid rgba(245, 197, 138, 0.25)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--accent-amber)',
              fontWeight: 700,
              fontSize: '1.1rem',
              marginBottom: '20px',
            }}>
              2
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
              <FolderOpen size={18} color="var(--accent-amber)" />
              <h3 style={{ fontSize: '1.2rem', color: 'var(--text-cream)' }}>Open Downloaded File</h3>
            </div>
            <p style={{ fontSize: '0.925rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              Open the downloaded <code style={{ color: 'var(--accent-amber)' }}>{APP_CONFIG.apkFileName}</code> file from your phone's notification bar or your <strong style={{ color: 'var(--text-cream)' }}>Downloads</strong> folder.
            </p>
          </div>

          {/* Step 3 */}
          <div className="glass-card" style={{ padding: '30px 24px', position: 'relative' }}>
            <div style={{
              width: '42px',
              height: '42px',
              borderRadius: '12px',
              background: 'rgba(245, 197, 138, 0.1)',
              border: '1px solid rgba(245, 197, 138, 0.25)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--accent-amber)',
              fontWeight: 700,
              fontSize: '1.1rem',
              marginBottom: '20px',
            }}>
              3
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
              <ShieldCheck size={18} color="var(--accent-amber)" />
              <h3 style={{ fontSize: '1.2rem', color: 'var(--text-cream)' }}>Grant Permission & Install</h3>
            </div>
            <p style={{ fontSize: '0.925rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              If Android asks for permission to install apps from that browser or file manager (<em style={{ color: 'var(--text-cream)' }}>“Install unknown apps”</em>), allow it for that installation and tap <strong style={{ color: 'var(--text-cream)' }}>Install</strong>.
            </p>
          </div>
        </div>

        {/* Clear Security Note Box */}
        <div style={{
          background: 'rgba(38, 31, 27, 0.65)',
          border: '1px solid var(--border-medium)',
          borderRadius: 'var(--radius-lg)',
          padding: '20px 16px',
          marginBottom: '36px',
          display: 'flex',
          gap: '14px',
          alignItems: 'flex-start',
        }}>
          <div style={{
            width: '36px',
            height: '36px',
            borderRadius: '10px',
            background: 'rgba(245, 197, 138, 0.12)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flexShrink: 0,
            marginTop: '2px',
          }}>
            <AlertTriangle size={18} color="var(--accent-amber)" />
          </div>
          <div>
            <h4 style={{ fontSize: '1rem', color: 'var(--text-cream)', marginBottom: '6px', fontWeight: 600 }}>
              Direct Distribution Security Note
            </h4>
            <p style={{ fontSize: '0.875rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>
              “Pocket Brother is distributed directly as an APK, outside Google Play. Android may display a security warning. Only install the APK if you trust its source. Review any permissions requested by the app before granting them.”
            </p>
            <p style={{ fontSize: '0.8rem', color: 'var(--text-subtle)', marginTop: '8px' }}>
              Tip: You do not need to disable Google Play Protect or compromise your device security settings.
            </p>
          </div>
        </div>

        {/* Troubleshooting Accordion */}
        <div style={{ maxWidth: '820px', margin: '0 auto' }}>
          <h3 style={{
            fontSize: '1.2rem',
            color: 'var(--text-cream)',
            marginBottom: '16px',
            textAlign: 'center',
          }}>
            Troubleshooting Common Installation Issues
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {troubleshootingItems.map((item, index) => {
              const IconComponent = item.icon;
              const isOpen = activeTroubleshoot === index;
              return (
                <div
                  key={index}
                  style={{
                    background: 'var(--surface-card)',
                    border: '1px solid var(--border-medium)',
                    borderRadius: 'var(--radius-md)',
                    overflow: 'hidden',
                    transition: 'border-color 0.2s',
                  }}
                >
                  <button
                    onClick={() => toggleTroubleshoot(index)}
                    style={{
                      width: '100%',
                      padding: '14px 16px',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      textAlign: 'left',
                      color: 'var(--text-cream)',
                      background: 'none',
                    }}
                  >
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                      <IconComponent size={18} color="var(--accent-amber)" style={{ flexShrink: 0 }} />
                      <span style={{ fontSize: '0.9rem', fontWeight: 600 }}>{item.title}</span>
                    </div>
                    {isOpen ? <ChevronUp size={18} color="var(--text-muted)" style={{ flexShrink: 0 }} /> : <ChevronDown size={18} color="var(--text-muted)" style={{ flexShrink: 0 }} />}
                  </button>

                  {isOpen && (
                    <div style={{
                      padding: '12px 16px 16px 16px',
                      color: 'var(--text-muted)',
                      fontSize: '0.865rem',
                      lineHeight: 1.6,
                      borderTop: '1px solid var(--border-subtle)',
                    }}>
                      {item.content}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>

      </div>
    </section>
  );
};
