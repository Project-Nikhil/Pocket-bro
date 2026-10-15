import { useState } from 'react';
import { Download, Smartphone, ShieldCheck, HardDrive, Cpu, Layers, HelpCircle, ChevronDown, ChevronUp, CheckCircle, Wrench } from 'lucide-react';
import { APP_CONFIG, APP_RELEASES, type AppRelease } from '../config/appConfig';

interface DownloadCardProps {
  onOpenBuildGuide?: () => void;
}

export const DownloadCard = ({ onOpenBuildGuide }: DownloadCardProps) => {
  const [showFixes, setShowFixes] = useState<boolean>(true);

  // Always keep and feature the latest new version on the main home page
  const latestRelease: AppRelease = APP_RELEASES.find(r => r.version === APP_CONFIG.version) || APP_RELEASES[0];

  const getCategoryColor = (category: string) => {
    switch (category) {
      case 'Feature': return { bg: 'rgba(52, 211, 153, 0.15)', text: '#34D399', border: 'rgba(52, 211, 153, 0.3)' };
      case 'Fix': return { bg: 'rgba(248, 113, 113, 0.15)', text: '#F87171', border: 'rgba(248, 113, 113, 0.3)' };
      case 'UI/UX': return { bg: 'rgba(142, 167, 233, 0.15)', text: '#8EA7E9', border: 'rgba(142, 167, 233, 0.3)' };
      default: return { bg: 'rgba(245, 197, 138, 0.15)', text: '#F5C58A', border: 'rgba(245, 197, 138, 0.3)' };
    }
  };

  return (
    <div id="download-versions" className="container" style={{ marginBottom: '48px' }}>
      <div className="download-spec-card">
        {/* Amber accent line */}
        <div style={{
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          height: '2px',
          background: 'linear-gradient(90deg, transparent 0%, var(--accent-amber) 50%, transparent 100%)',
        }} />

        <div className="download-card-body">
          {/* Top Status & Verified Badge */}
          <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            flexWrap: 'wrap',
            gap: '8px',
            marginBottom: '16px',
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span className="pill-badge" style={{ fontSize: '0.75rem', padding: '3px 10px' }}>
                <ShieldCheck size={13} color="#34D399" />
                Verified Android APK
              </span>
              <span style={{ fontSize: '0.8rem', color: 'var(--text-subtle)' }}>
                Target SDK 36 • Android 7.0+
              </span>
            </div>
            
            <div style={{
              fontSize: '0.78rem',
              color: 'var(--accent-amber)',
              fontWeight: 600,
              background: 'rgba(245, 197, 138, 0.1)',
              padding: '3px 10px',
              borderRadius: 'var(--radius-full)',
              border: '1px solid var(--border-accent)',
            }}>
              v{latestRelease.version} Latest Release ✨
            </div>
          </div>

          {/* Quick Metrics Ribbon (App Store style for mobile) */}
          <div className="spec-metrics-grid">
            <div className="metric-item">
              <div className="metric-label">
                <HardDrive size={11} />
                <span>Size</span>
              </div>
              <div className="metric-val" style={{ color: 'var(--accent-amber)' }}>
                {latestRelease.apkSize}
              </div>
            </div>

            <div className="metric-item">
              <div className="metric-label">
                <Layers size={11} />
                <span>Version</span>
              </div>
              <div className="metric-val">
                v{latestRelease.version}
              </div>
            </div>

            <div className="metric-item">
              <div className="metric-label">
                <Smartphone size={11} />
                <span>OS</span>
              </div>
              <div className="metric-val">
                Android 7+
              </div>
            </div>

            <div className="metric-item">
              <div className="metric-label">
                <Cpu size={11} />
                <span>Engine</span>
              </div>
              <div className="metric-val">
                Gemma + Core
              </div>
            </div>
          </div>

          {/* Primary CTA & Direct Link */}
          <div style={{ marginTop: '20px' }}>
            <a
              href={latestRelease.apkDownloadPath}
              download={latestRelease.apkFileName}
              className="btn-primary"
              id="download-card-btn"
              style={{
                width: '100%',
                justifyContent: 'center',
                padding: '14px 20px',
                fontSize: '0.975rem',
              }}
            >
              <Download size={18} />
              <span>Download v{latestRelease.version} APK ({latestRelease.apkSize})</span>
            </a>

            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              flexWrap: 'wrap',
              gap: '6px',
              fontSize: '0.8rem',
              color: 'var(--text-subtle)',
              marginTop: '10px',
            }}>
              <span>Direct link: </span>
              <a
                href={latestRelease.apkDownloadPath}
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: 'var(--accent-amber)', textDecoration: 'underline' }}
              >
                {latestRelease.apkFileName}
              </a>
              {onOpenBuildGuide && (
                <>
                  <span> • </span>
                  <button
                    onClick={onOpenBuildGuide}
                    style={{
                      color: 'var(--text-muted)',
                      textDecoration: 'underline',
                      display: 'inline-flex',
                      alignItems: 'center',
                      gap: '4px',
                      fontSize: 'inherit',
                    }}
                  >
                    <HelpCircle size={12} />
                    <span>How it works</span>
                  </button>
                </>
              )}
            </div>
          </div>

          {/* Release Notes & Fixes Drawer */}
          <div style={{
            marginTop: '22px',
            borderTop: '1px solid var(--border-medium)',
            paddingTop: '16px',
          }}>
            <button
              onClick={() => setShowFixes(!showFixes)}
              style={{
                width: '100%',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                padding: '8px 0',
                color: 'var(--text-cream)',
                textAlign: 'left',
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Wrench size={16} color="var(--accent-amber)" />
                <span style={{ fontSize: '0.95rem', fontWeight: 600 }}>
                  What's in v{latestRelease.version} & Release Fixes
                </span>
                <span style={{
                  fontSize: '0.72rem',
                  padding: '2px 8px',
                  borderRadius: 'var(--radius-full)',
                  background: 'rgba(245, 197, 138, 0.1)',
                  color: 'var(--accent-amber)',
                  fontWeight: 600,
                }}>
                  {latestRelease.fixes.length} changes
                </span>
              </div>
              {showFixes ? <ChevronUp size={18} color="var(--text-muted)" /> : <ChevronDown size={18} color="var(--text-muted)" />}
            </button>

            {showFixes && (
              <div style={{ marginTop: '14px', display: 'flex', flexDirection: 'column', gap: '10px' }}>
                {/* Summary banner */}
                <div style={{
                  padding: '12px 14px',
                  background: 'rgba(26, 20, 17, 0.7)',
                  borderRadius: 'var(--radius-md)',
                  border: '1px solid var(--border-subtle)',
                  fontSize: '0.85rem',
                  color: 'var(--text-muted)',
                  lineHeight: 1.5,
                }}>
                  <strong style={{ color: 'var(--text-cream)' }}>{latestRelease.headline}: </strong>
                  {latestRelease.summary}
                </div>

                {/* Highlights List */}
                <div style={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '6px',
                  margin: '6px 0',
                }}>
                  {latestRelease.highlights.map((h, i) => (
                    <div key={i} style={{ display: 'flex', alignItems: 'flex-start', gap: '8px', fontSize: '0.825rem', color: 'var(--text-cream)' }}>
                      <CheckCircle size={14} color="#34D399" style={{ flexShrink: 0, marginTop: '2px' }} />
                      <span>{h}</span>
                    </div>
                  ))}
                </div>

                {/* Detailed Fixes Breakdown */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '4px' }}>
                  {latestRelease.fixes.map((fix, idx) => {
                    const catStyle = getCategoryColor(fix.category);
                    return (
                      <div
                        key={idx}
                        style={{
                          background: 'rgba(32, 25, 21, 0.55)',
                          border: '1px solid var(--border-subtle)',
                          borderRadius: 'var(--radius-md)',
                          padding: '10px 12px',
                        }}
                      >
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: '8px', marginBottom: '4px' }}>
                          <span style={{ fontSize: '0.875rem', fontWeight: 600, color: 'var(--text-cream)' }}>
                            {fix.title}
                          </span>
                          <span style={{
                            fontSize: '0.675rem',
                            fontWeight: 700,
                            padding: '2px 7px',
                            borderRadius: 'var(--radius-full)',
                            background: catStyle.bg,
                            color: catStyle.text,
                            border: `1px solid ${catStyle.border}`,
                            textTransform: 'uppercase',
                            flexShrink: 0,
                          }}>
                            {fix.category}
                          </span>
                        </div>
                        <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', lineHeight: 1.45 }}>
                          {fix.description}
                        </p>
                      </div>
                    );
                  })}
                </div>

              </div>
            )}
          </div>

        </div>
      </div>

      <style>{`
        .download-spec-card {
          background: linear-gradient(135deg, #1F1916 0%, #261F1B 100%);
          border: 1px solid var(--border-medium);
          border-radius: var(--radius-lg);
          padding: 20px 18px;
          position: relative;
          overflow: hidden;
          box-shadow: var(--shadow-card);
        }

        .spec-metrics-grid {
          display: grid;
          grid-template-columns: repeat(4, 1fr);
          gap: 8px;
          background: rgba(18, 14, 12, 0.6);
          border: 1px solid var(--border-subtle);
          border-radius: var(--radius-md);
          padding: 12px 10px;
        }

        @media (max-width: 480px) {
          .spec-metrics-grid {
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
          }
        }

        .metric-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          text-align: center;
        }

        .metric-label {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 0.72rem;
          color: var(--text-subtle);
          text-transform: uppercase;
          letter-spacing: 0.04em;
        }

        .metric-val {
          font-size: 0.925rem;
          font-weight: 700;
          color: var(--text-cream);
          margin-top: 2px;
        }

        @media (min-width: 768px) {
          .download-spec-card {
            padding: 26px 28px;
            border-radius: var(--radius-xl);
          }
        }
      `}</style>
    </div>
  );
};
