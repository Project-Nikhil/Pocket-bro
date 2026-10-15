import React from 'react';
import { Heart } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

interface FooterProps {
  onOpenBuildGuide?: () => void;
}

export const Footer: React.FC<FooterProps> = ({ onOpenBuildGuide }) => {
  return (
    <footer className="footer-container" style={{
      backgroundColor: '#0F0D0C',
      borderTop: '1px solid var(--border-medium)',
      padding: '50px 0 90px',
    }}>
      <div className="container">
        
        {/* Top Footer Row */}
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '24px',
          alignItems: 'center',
          textAlign: 'center',
          marginBottom: '32px',
        }} className="footer-top-row">

          {/* Logo / Wordmark */}
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px' }}>
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '8px',
              fontSize: '1.4rem',
              fontWeight: 700,
              color: 'var(--text-cream)',
            }}>
              <span>Pocket Brother</span>
              <Heart size={20} fill="#F87171" color="#F87171" />
            </div>
            <div style={{
              color: 'var(--accent-amber)',
              fontSize: '0.95rem',
              fontWeight: 500,
            }}>
              “made with love ♡”
            </div>
          </div>

          {/* Navigation Links */}
          <nav style={{
            display: 'flex',
            flexWrap: 'wrap',
            justifyContent: 'center',
            gap: '24px',
            fontSize: '0.925rem',
          }}>
            <a href={APP_CONFIG.apkDownloadPath} download={APP_CONFIG.apkFileName} style={{ color: 'var(--text-cream)', fontWeight: 600 }}>
              Download for Android
            </a>
            <a href="#features" style={{ color: 'var(--text-muted)' }}>
              Features
            </a>
            <a href="#install" style={{ color: 'var(--text-muted)' }}>
              Installation Guide
            </a>
            <a href="#privacy" style={{ color: 'var(--text-muted)' }}>
              Privacy
            </a>
            <a href="#faq" style={{ color: 'var(--text-muted)' }}>
              FAQ
            </a>
            {onOpenBuildGuide && (
              <button
                onClick={onOpenBuildGuide}
                style={{ color: 'var(--accent-amber)', background: 'none' }}
              >
                Brother's Build Guide
              </button>
            )}
          </nav>
        </div>

        {/* Divider */}
        <div style={{ height: '1px', background: 'var(--border-subtle)', marginBottom: '24px' }} />

        {/* Bottom Note & Version Info */}
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: '10px',
          textAlign: 'center',
          fontSize: '0.8rem',
          color: 'var(--text-subtle)',
        }}>
          <div>
            Pocket Brother Version {APP_CONFIG.version} (Build {APP_CONFIG.versionCode}) • Package: <code>{APP_CONFIG.packageId}</code>
          </div>
          <div style={{ maxWidth: '600px', lineHeight: 1.5 }}>
            “An independently distributed Android application. Not affiliated with or endorsed by Google.”
          </div>
          <div style={{ marginTop: '8px', color: '#554B43' }}>
            Built for sisters · Handcrafted with care
          </div>
        </div>

      </div>
    </footer>
  );
};
